/*
 * Copyright (c) 2022-2023, NVIDIA CORPORATION.
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
{"spark": "311"}
{"spark": "312"}
{"spark": "313"}
{"spark": "320"}
{"spark": "321"}
{"spark": "321cdh"}
{"spark": "321db"}
{"spark": "322"}
{"spark": "323"}
{"spark": "324"}
{"spark": "330"}
{"spark": "330cdh"}
{"spark": "331"}
{"spark": "332"}
{"spark": "332cdh"}
{"spark": "333"}
spark-rapids-shim-json-lines ***/
package org.apache.spark.sql.rapids

import com.nvidia.spark.rapids._
import com.nvidia.spark.rapids.shims.ShimExpression

import org.apache.spark.sql.catalyst.expressions.{Expression, NullIntolerant}
import org.apache.spark.sql.internal.SQLConf
import org.apache.spark.sql.types._

abstract class CudfBinaryArithmetic extends CudfBinaryOperator with NullIntolerant {
  override def dataType: DataType = left.dataType
  // arithmetic operations can overflow and throw exceptions in ANSI mode
  override def hasSideEffects: Boolean = super.hasSideEffects || SQLConf.get.ansiEnabled
}

case class GpuIntegralDivide(
    left: Expression,
    right: Expression) extends GpuIntegralDivideParent(left, right)

case class GpuDecimalDivide(
    left: Expression,
    right: Expression,
    override val dataType: DecimalType,
    failOnError: Boolean = SQLConf.get.ansiEnabled) extends ShimExpression
    with GpuDecimalDivideBase {
  override def integerDivide = false

  override def children: Seq[Expression] = Seq(left, right)
}

case class GpuDecimalMultiply(
    left: Expression,
    right: Expression,
    dataType: DecimalType,
    useLongMultiply: Boolean = false,
    failOnError: Boolean = SQLConf.get.ansiEnabled) extends ShimExpression
    with GpuDecimalMultiplyBase {

  override def children: Seq[Expression] = Seq(left, right)
}

case class GpuAdd(
    left: Expression,
    right: Expression,
    failOnError: Boolean) extends GpuAddBase(failOnError)

case class GpuSubtract(
    left: Expression,
    right: Expression,
    failOnError: Boolean) extends GpuSubtractBase(failOnError)

case class GpuRemainder(left: Expression, right: Expression) extends GpuRemainderBase(left, right)

case class GpuPmod(left: Expression, right: Expression) extends GpuPmodBase(left, right)
