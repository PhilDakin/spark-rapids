/*
 * Copyright (c) 2023, NVIDIA CORPORATION.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*** spark-rapids-shim-json-lines
{"spark": "340"}
{"spark": "341"}
{"spark": "341db"}
{"spark": "350"}
spark-rapids-shim-json-lines ***/
package org.apache.spark.sql.catalyst.json

import com.nvidia.spark.rapids.shims.LegacyBehaviorPolicyShim

import org.apache.spark.sql.catalyst.util.DateFormatter

object GpuJsonUtils {
  def dateFormatInRead(options: JSONOptions): String =
    options.dateFormatInRead.getOrElse(DateFormatter.defaultPattern)

  def timestampFormatInRead(options: JSONOptions): String = options.timestampFormatInRead.getOrElse(
    if (LegacyBehaviorPolicyShim.isLegacyTimeParserPolicy()) {
      s"${DateFormatter.defaultPattern}'T'HH:mm:ss.SSSXXX"
    } else {
      s"${DateFormatter.defaultPattern}'T'HH:mm:ss[.SSS][XXX]"
    })

  def enableDateTimeParsingFallback(options: JSONOptions): Boolean =
    options.enableDateTimeParsingFallback.getOrElse(false)
}
