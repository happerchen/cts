; Copyright (C) 2008 The Android Open Source Project
;
; Licensed under the Apache License, Version 2.0 (the "License");
; you may not use this file except in compliance with the License.
; You may obtain a copy of the License at
;
;      http://www.apache.org/licenses/LICENSE-2.0
;
; Unless required by applicable law or agreed to in writing, software
; distributed under the License is distributed on an "AS IS" BASIS,
; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
; See the License for the specific language governing permissions and
; limitations under the License.

.source T_sput_byte_9.java
.class public dot.junit.opcodes.sput_byte.d.T_sput_byte_9
.super java/lang/Object

.field public st_i1 B

.method public <init>()V
.limit regs 1

       invoke-direct {v0}, java/lang/Object/<init>()V
       return-void
.end method

.method public run()V
.limit regs 3

       const v1, 0
       sput-byte v1, dot.junit.opcodes.sput_byte.d.T_sput_byte_9noclass.st_i1 B
       return-void
.end method


