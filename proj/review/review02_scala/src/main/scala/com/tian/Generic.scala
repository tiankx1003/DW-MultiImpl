package com.tian

/**
 * @author tiankx
 * @date 2020/10/10 17:07
 * @version 1.0.0
 */
object Generic {
  // 提供隐式值
  implicit val ord: Ordering[Users] = new Ordering[Users] {
    override def compare(x: Users, y: Users): Int = return x.age.-(y.age)
  }

  def main(args: Array[String]): Unit = {

    val point1 = new Point(10, 20)
    val point2 = new Point[Int](10, 20)
    //    val point3 = new Point[Int](10, 20.0)
    val animal = new Animal
    val pet = new Pet
    val lion = new Lion
    val cat = new Cat
    val dog = new Dog
    val petC = new PetContainer(new Animal)

    Generic.f1(10, 20)(Ordering.Int)
    Generic.f1(10, 20)(Ordering.Int)
    Generic.f2("aa", "bb")(Ordering.String)
    Generic.f2("aa", "bb")(Ordering.String)
    Predef.println(Generic.f2(new Users(20), new Users(30))(Generic.ord))
    Predef.println("Hello World!")

    val arr1 = Array.apply(10, 20)
    //    val arr2:Array[Double] = arr1 //类型不匹配
    val bs = Array.apply(new B)
    //    val as:Array[A] = bs // 不变，类型不匹配
    val cb = new C[B]
    //    val ca :C[A] = cb //不变
    val db = new D[B]
    val da: D[A] = db // 协变
    val ea = new E[A]
    val eb: E[B] = ea //逆变
  }

  /** 柯里化、隐式值 */
  def f1[T](a: T, b: T)(implicit ord: Ordering[T]) = if (ord.gt(a, b)) a else b

  def f2[T: Ordering](a: T, b: T) = {
    val ord = Predef.implicitly(())[Ordering[T]]
    if (ord.gt(a, b)) a else b
  }

  def print(petContainer: PetContainer[Pet]): Unit = Predef.println(petContainer.pet.name)

  def f0[T](s: String) = Predef.println(s)

  /** 通过设置泛型上界从而继承compareTo方法 */
  def max[T <: Comparable[T]](a: T, b: T) = if (a.compareTo(b).>(0)) a else b

  /** 通过设置上界继承Ordered中的方法，可以直接使用运算符比较大小 */
  def min[T <: Ordered[T]](a: T, b: T) = if (a.>(b)) b else a


}

class A

class B extends A

/** 不变，不能把子类型对象的集合赋值给附类型的集合 */
class C[T]

/** 协变，把子类型对象的集合赋值给父类型的集合 */
class D[+T]

/** 逆变，把父类型的集合赋值给子类型的集合 */
class E[-T]

case class Users(val age: Int)

/**
 * 泛型类
 *
 * @param a 属性a
 * @param b 属性b
 * @tparam T 泛型
 */
class Point[T](val a: T, val b: T) {
  def f1(a: T): T = return a
}

/**
 * 样例类继承Ordered类并重写compare方法实现比较规则
 *
 * @param age
 */
case class User(val age: Int) extends Ordered[User] {
  override def compare(that: User): Int = return this.age.-(that.age)
}

class Animal {
  val name: String = "animal"
}

class Pet extends Animal {
  override val name: String = "pet"
}

class Lion extends Animal {
  override val name: String = "lion"
}

class Cat extends Pet {
  override val name: String = "cat"
}

class Dog extends Pet {
  override val name: String = "dog"
}

class PetContainer[T >: Pet](val pet: T)