<!---
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->
Pepperdata's Fork of Apache Commons Compress
===================

## Motivation

We have been using an internally modified version of Apache Commons Compress v. 1.5
for a very long time, and it is overdue for an upgrade.  While we do want to migrate to
a modern version of Commons Compress, we also do not want to lose the changes we had
to make to v. 1.5, _if_ they are still relevant.  Hence this fork, going forward.

## Main Documentation

For details regarding the original project, please refer to [README-orig](./README-orig.md).

## PD Current Status

The Commons Compress version chosen for the immediate future within our code base is the
latest one as of this writing, 1.26.2.  While the `master` branch is the _default_
branch, all changes so far have been made to the `commons-compress-1.26.2` branch.
The latter is the branch you should use (and modify) going forward, _until_ we decide to
move to another branch:

```shell
git clone git@github.com:/pepperdata/commons-compress.git -b commons-compress-1.26.2
```

### NOTES

- This project _requires_ at least Maven 3.6.3 to build, so please make provisions for that.
I have tested the build with Maven 3.6.3.
- There are a couple of unit tests that simply hang on most of our Linux build machines, but
**work and pass** on `build14`!  Since these tests are for modules our code _does not use at all_,
and since this happens on the _original_ Commons Compress 1.26.2 code (i.e., without PD mods),
this is not a major concern, but should be investigated in the future.  In the meantime,
fully passing Linux builds can be executed on `build14` (on our other Linux build machines,
these tests are automatically skipped).

## Differences Between the PD-specific and Stock 1.5 Versions

To view these, please browse the `./v.1.5-diffs` directory.

## License

This code is licensed under the [Apache License v2](https://www.apache.org/licenses/LICENSE-2.0).

See the `NOTICE.txt` file for required notices and attributions.
