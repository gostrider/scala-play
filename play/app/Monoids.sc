trait Monoid[A] {
  def mZero: A

  def `++`(a: A, b: A): A
}

implicit val stringMonoid = new Monoid[String] {
  override def mZero: String = ""

  override def `++`(a: String, b: String): String = a + b
}

implicit val intMonoid = new Monoid[Int] {
  override def mZero: Int = 0

  override def `++`(a: Int, b: Int): Int = a + b
}

def mConcat[A](elems: Seq[A])(implicit ev: Monoid[A]): A = {
  elems.foldLeft(ev.mZero)(ev.`++`)
}

mConcat(Seq("1", "2", "3"))
mConcat(Seq(1, 2, 3))