import scala.concurrent.Future

/**
  * Created by erikcom on 28/3/2017.
  */

trait Container[M[_]] {
  def put[A](x: A): M[A]

  def get[A](m: M[A]): A
}

val container = new Container[List] {
  override def put[A](x: A): List[A] = List(x)

  override def get[A](m: List[A]): A = m.head
}

implicit val listContainer = new Container[List] {
  override def put[A](x: A): List[A] = List(x)

  override def get[A](m: List[A]): A = m.head
}

implicit val optionContainer = new Container[Some] {
  override def put[A](x: A): Some[A] = Some(x)

  override def get[A](m: Some[A]): A = m.get
}

def makePair[M[_] : Container, A, B](fst: M[A], snd: M[B]) = {
  val c = implicitly[Container[M]]
  c.put(c.get(fst), c.get(snd))
}

case class FutureOption[A](inner: Future[Option[A]]) {
  // List[A]
  // def map[B](f: A => B): List[B]

  // Option[A]
  // def map[B](f: A => B): option[B]

  // Future[B]
  // def map[B](f: A => B): Future[B]

  def map[B](f: A => B): FutureOption[B] =
    FutureOption(inner.map(_.map(f)))

  // List[A]
  // def flatMap[B](f: A => List[B]): List[B]

  // Option[A]
  // def flatMap[B](f: A => Option[B]): Option[B]

  // Future[A]
  // def flatMap[B](f: A => Option[B]): Future[B]

  def flatMap[B](f: A => FutureOption[B]): FutureOption[B] = FutureOption(
    inner.flatMap {
      case Some(a) => f(a).inner
      case None => Future.successful(None)
    }
  )
}

object HigherKindedType {
  def getX: Option[Int] = Some(5)

  def getY: Option[Int] = Some(1)

  val z1: Option[Int] = getX flatMap { x => getY map { y => x + y } }

  val z2: Option[Int] = for {
    x <- getX
    y <- getY
  } yield x + y

  def getFX: Future[Option[Int]] = Future(Some(5))

  def getFY: Future[Option[Int]] = Future(Some(1))

  val fz1: Future[Option[Int]] =
    getFX.flatMap { xOpt => getFY.map { yOpt => xOpt.flatMap { x => yOpt.map { y => x + y } } } }

  val fz2: Future[Option[Int]] = getFX.flatMap {
    case None => Future.successful(None)
    case Some(x) => getFY.map {
      case None => None
      case Some(y) => Some(x + y)
    }
  }

  val fz3: FutureOption[Int] = for {
    x <- FutureOption(getFX)
    y <- FutureOption(getFY)
  } yield x + y

  val fz4: Future[Option[Int]] = fz3.inner
}

trait Monad[M[_]] {
  def `return`[A](a: A): M[A]

  def map[A, B](ma: M[A])(f: A => B): M[B]

  // (>>=) :: m a -> ( a -> m b) -> m b
  def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]
}

case class AnyMOption[M[_], A](inner: M[Option[A]])(implicit m: Monad[M]) {
  def map[B](f: A => B): AnyMOption[M, B] =
    AnyMOption(m.map(inner)(_.map(f)))

  def flatMap[B](f: A => AnyMOption[M, B]): AnyMOption[M, B] =
    AnyMOption(m.flatMap(inner) {
      case Some(a) => f(a).inner
      case None => m `return` None
    })
}
