sealed trait SomeType

case class TypeA(name: String) extends SomeType

case class TypeB(name: Int) extends SomeType

case class TypeC(name: Boolean) extends SomeType

val typeA: SomeType = TypeA("a")
val typeB: SomeType = TypeB(1)
val typeC: SomeType = TypeC(true)

println(typeA)

val nums = Seq(1, 2, 3)

nums.find(_ == 1) // Some(1)
nums.find(_ == 5) // None

val words = Seq("aa", "bb", "cc")

words.find(_ == "aa")