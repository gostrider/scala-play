import scalaz.Scalaz._


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

val someOps = (k: String) => words.find(_ == k)

val result = for {
  res <- someOps("aa") \/> "1: key not found"
  res2 <- someOps(res) \/> "2: key not found"
} yield (res, res2)

println(result)

// Dependent types
trait Key {
  type Value
}

trait HMap {
  def get(k: Key): Option[k.Value]

  def add(k: Key)(value: k.Value): HMap
}

val sort = new Key {
  type Value = String
}
val width = new Key {
  type Value = Int
}

Option(1).flatMap { i => if (i < 0) None else Some(i + 1) }
// Wrapped twice
Option(1).map { i => if (i < 0) 0 else i + 1 }
Option(1).map { i => if (i < 0) None else Some(i + 1) }
