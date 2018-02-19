package com.ajjpj.aoffice.util


case class Unit(factorToMm: Double) {
  val factorFromMm = 1 / factorToMm
}
object Unit {
  val mm = Unit(1)
  val inch = Unit(25.4)
  val point = Unit(inch.factorToMm / 72)
}

case class Size(l: Double, unit: Unit) {
  def convertTo (u: Unit) =
    if (u == unit)
      this
    else
      Size(l * unit.factorToMm * u.factorFromMm, u)

  def +(other: Size) = Size (l + other.convertTo(unit).l, unit)
  def -(other: Size) = Size (l - other.convertTo(unit).l, unit)
}
object Size {
  val Zero = Size(0, Unit.point)
}