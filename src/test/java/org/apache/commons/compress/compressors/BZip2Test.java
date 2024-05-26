/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.compress.compressors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.Arrays;

import org.apache.commons.compress.AbstractTest;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

@SuppressWarnings("ResultOfMethodCallIgnored")
public final class BZip2Test extends AbstractTest {

    @Test
    public void testBzip2Unarchive() throws Exception {
        final File input = getFile("bla.txt.bz2");
        final File output = newTempFile("bla.txt");
        try (InputStream is = Files.newInputStream(input.toPath());
                CompressorInputStream in =
                    new CompressorStreamFactory().createCompressorInputStream("bzip2", is)) {
            Files.copy(in, output.toPath());
        }
    }

    @Test
    public void testBzipCreation() throws Exception {
        File output;
        final File input = getFile("test.txt");
        {
            output = newTempFile("test.txt.bz2");
            try (OutputStream out = Files.newOutputStream(output.toPath());
                    CompressorOutputStream cos =
                        new CompressorStreamFactory().createCompressorOutputStream("bzip2", out)) {
                Files.copy(input.toPath(), cos);
            }
        }

        final File decompressed = newTempFile("decompressed.txt");
        {
            try (InputStream is = Files.newInputStream(output.toPath());
                    CompressorInputStream in =
                        new CompressorStreamFactory().createCompressorInputStream("bzip2", is)) {
                Files.copy(in, decompressed.toPath());
            }
        }

        assertEquals(input.length(), decompressed.length());
    }

    @Test
    public void testCOMPRESS131() throws Exception {
        final File input = getFile("COMPRESS-131.bz2");
        try (InputStream is = Files.newInputStream(input.toPath())) {
            try (CompressorInputStream in = new BZip2CompressorInputStream(is, true)) {
                int loc = 0;
                while (in.read() != -1) {
                    loc++;
                }
                assertEquals(539, loc);
            }
        }
    }

    @Test
    public void testConcatenatedStreamsReadFirstOnly() throws Exception {
        final File input = getFile("multiple.bz2");
        try (InputStream is = Files.newInputStream(input.toPath())) {
            try (CompressorInputStream in =
                     new CompressorStreamFactory().createCompressorInputStream("bzip2", is)) {
                assertEquals('a', in.read());
                assertEquals(-1, in.read());
            }
        }
    }

    @Test
    public void testConcatenatedStreamsReadFully() throws Exception {
        final File input = getFile("multiple.bz2");
        try (InputStream is = Files.newInputStream(input.toPath())) {
            try (CompressorInputStream in = new BZip2CompressorInputStream(is, true)) {
                assertEquals('a', in.read());
                assertEquals('b', in.read());
                assertEquals(0, in.available());
                assertEquals(-1, in.read());
            }
        }
    }

    @SuppressWarnings("SimplifiableAssertion")
    private void assert_bzip2_file_equals(File bz2file, byte[] data) throws IOException {
        // Deliberately do this with bzip2 command line, to better test for customers.
        Runtime rt = Runtime.getRuntime();
        String command = "bzip2 -cd " + bz2file.getAbsolutePath();
        Process p = rt.exec(command);
        System.out.printf("Ran %s%n", command);
        byte[] new_out = read_fully(p.getInputStream());
        byte[] new_err = read_fully(p.getErrorStream());

        assertTrue(new_err.length == 0, "stdout printed: " + new String(new_err));
        assertTrue(Arrays.equals(new_out, data),
            "Arrays not equal:\n" + Arrays.toString(new_out) + "\n" + Arrays.toString(data));
    }

    @Test
    public void testBzipFlush() throws IOException {
        final File output = new File(tempResultDir, "test-flush.bz2");
        output.delete();
        final File input = getFile("testCompress209.doc");
        final RandomAccessFile out = new RandomAccessFile(output, "rw");
        final BZip2CompressorOutputStream cos = new BZip2CompressorOutputStream(out.getChannel());
        byte[] in_data = read_fully(input);

        cos.write(in_data, 0, 100);
        cos.flush();
        assert_bzip2_file_equals(output, Arrays.copyOfRange(in_data, 0, 100));

        cos.write(in_data, 100, 100);
        cos.flush();
        assert_bzip2_file_equals(output, Arrays.copyOfRange(in_data, 0, 200));

        cos.write(in_data, 200, in_data.length - 200);
        cos.close(); //Note close, not flush
        assert_bzip2_file_equals(output, in_data);
    }

    @Test
    public void testBzipFlushFlushClose() throws IOException {
        final File output = new File(tempResultDir, "test-ffc.bz2");
        output.delete();
        final RandomAccessFile out = new RandomAccessFile(output, "rw");
        final BZip2CompressorOutputStream cos = new BZip2CompressorOutputStream(out.getChannel());
        byte[] in_data =
            {16, 10, 5, 116, 97, 105, 100, 49, 16, -87, -103, -80, -54, -124, 39, 26, 0};
        cos.printInternalState("pre-write");
        cos.write(in_data, 0, 17);
        cos.printInternalState("post-write");
        cos.flush();
        cos.printInternalState("post-flush1");
        cos.flush();
        cos.printInternalState("post-flush2");
        cos.close();
        cos.printInternalState("post-close");
        assert_bzip2_file_equals(output, in_data);
    }

    @Test
    public void testBzipNoFlushClose() throws IOException {
        final File output = new File(tempResultDir, "test-nfc.bz2");
        output.delete();
        final RandomAccessFile out = new RandomAccessFile(output, "rw");
        final BZip2CompressorOutputStream cos = new BZip2CompressorOutputStream(out.getChannel());
        byte[] in_data =
            {16, 10, 5, 116, 97, 105, 100, 49, 16, -87, -103, -80, -54, -124, 39, 26, 0};
        cos.printInternalState("pre-write");
        cos.write(in_data, 0, 17);
        cos.printInternalState("post-write");
        cos.close();
        cos.printInternalState("post-close");
        assert_bzip2_file_equals(output, in_data);
    }

    @Test
    public void testBzipFlushClose() throws IOException {
        final File output = new File(tempResultDir, "test-fc.bz2");
        output.delete();
        final RandomAccessFile out = new RandomAccessFile(output, "rw");
        final BZip2CompressorOutputStream cos = new BZip2CompressorOutputStream(out.getChannel());
        byte[] in_data =
            {16, 10, 5, 116, 97, 105, 100, 49, 16, -87, -103, -80, -54, -124, 39, 26, 0};
        cos.printInternalState("pre-write");
        cos.write(in_data, 0, 17);
        cos.printInternalState("post-write");
        cos.flush();
        cos.printInternalState("post-flush");
        cos.close();
        cos.printInternalState("post-close");
        assert_bzip2_file_equals(output, in_data);
    }

    @Test
    public void testBzipNoWrite() throws IOException {
        final File output = new File(tempResultDir, "test-nw.bz2");
        output.delete();
        final RandomAccessFile out = new RandomAccessFile(output, "rw");
        final BZip2CompressorOutputStream cos =
            new BZip2CompressorOutputStream(out.getChannel());
        cos.close();
        cos.printInternalState("post-close");
        assert_bzip2_file_equals(output, new byte[0]);
    }

    @Test
    public void testBzipNoWriteFlush() throws IOException {
        final File output = new File(tempResultDir, "test-nwf.bz2");
        output.delete();
        final RandomAccessFile out = new RandomAccessFile(output, "rw");
        final BZip2CompressorOutputStream cos =
            new BZip2CompressorOutputStream(out.getChannel());
        cos.flush();
        assert_bzip2_file_equals(output, new byte[0]);
        cos.close();
        cos.printInternalState("post-close");
        assert_bzip2_file_equals(output, new byte[0]);
    }

    private byte[] read_fully(InputStream in) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(in, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private byte[] read_fully(File input) throws IOException {
        FileInputStream in = new FileInputStream(input);
        return read_fully(in);
    }
}
