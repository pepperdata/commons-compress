// Copyright (c) 2018 Pepperdata Inc. - All rights reserved.

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
package org.apache.commons.compress.compressors.bzip2;

import java.io.IOException;

/**
 * A checked exception to specifically address BZip2 compression package-specific errors
 * (as opposed to general I/O exceptions originating from standard I/O operations).
 */
public class BZip2CompressorException extends IOException {

    private static final long serialVersionUID = -3635648540624875876L;

    /**
     * Constructs an {@code BZip2CompressorException} with {@code null}
     * as its error detail message.
     */
    public BZip2CompressorException() {
        super();
    }

    /**
     * Constructs an {@code BZip2CompressorException} with the specified detail message.
     *
     * @param message
     *        The detail message (which is saved for later retrieval by the
     *        {@link #getMessage()} method)
     */
    public BZip2CompressorException(String message) {
        super(message);
    }

    /**
     * Constructs an {@code BZip2CompressorException} with the specified detail message
     * and cause.
     *
     * <p> Note that the detail message associated with {@code cause} is
     * <i>not</i> automatically incorporated into this exception's detail
     * message.
     *
     * @param message
     *        The detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method)
     *
     * @param cause
     *        The cause (which is saved for later retrieval by the
     *        {@link #getCause()} method).  (A null value is permitted,
     *        and indicates that the cause is nonexistent or unknown.)
     */
    public BZip2CompressorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a {@code BZip2CompressorException} with the specified cause and a
     * detail message of {@code (cause==null ? null : cause.toString())}
     * (which typically contains the class and detail message of {@code cause}).
     * This constructor is useful for exceptions that are little more than wrappers
     * for other throwables.
     *
     * @param cause
     *        The cause (which is saved for later retrieval by the
     *        {@link #getCause()} method).  (A null value is permitted,
     *        and indicates that the cause is nonexistent or unknown.)
     *
     * @since 1.6
     */
    public BZip2CompressorException(Throwable cause) {
        super(cause);
    }
}
