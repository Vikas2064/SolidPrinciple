# Liskov Substitution Principle (LSP)

## What is LSP?

> **Objects of a subclass should be replaceable with objects of its superclass without breaking the correctness of the program.**

In simple words:

> **If `B` is a subtype of `A`, we should be able to use `B` wherever we expect `A` without unexpected behavior.**

---

# ❌ Bad Example

Suppose we have a `Bike` class:

```java
class Bike {

    public void startEngine() {
        System.out.println("Engine started");
    }
}
```

Now we create a `MotorBike`:

```java
class MotorBike extends Bike {

    @Override
    public void startEngine() {
        System.out.println("MotorBike engine started");
    }
}
```

And a `Bicycle`:

```java
class Bicycle extends Bike {

    @Override
    public void startEngine() {
        throw new UnsupportedOperationException(
            "Bicycle does not have an engine"
        );
    }
}
```

At first, this looks reasonable:

```text
        Bike
       /    \
      /      \
MotorBike  Bicycle
```

But there is a problem.

---

## What is the problem?

Suppose we have a method that expects a `Bike`:

```java
class BikeService {

    public static void startBike(Bike bike) {
        bike.startEngine();
    }
}
```

With a `MotorBike`:

```java
Bike bike = new MotorBike();

BikeService.startBike(bike);
```

Output:

```text
MotorBike engine started
```

Everything works.

But now replace `MotorBike` with `Bicycle`:

```java
Bike bike = new Bicycle();

BikeService.startBike(bike);
```

This throws:

```text
UnsupportedOperationException
```

The problem is that `BikeService` expects a `Bike` to support:

```java
startEngine()
```

But `Bicycle` cannot fulfill that expectation.

Therefore, `Bicycle` cannot properly substitute `Bike`.

This violates **LSP**.

---

# Why does this violate LSP?

The whole idea of LSP is:

```text
Parent
  ↓
Child should be usable wherever Parent is expected
```

We wrote:

```java
Bike bike = new Bicycle();
```

So `Bicycle` should behave correctly wherever a `Bike` is expected.

But:

```java
BikeService.startBike(bike);
```

breaks when `bike` is actually a `Bicycle`.

Therefore:

> **Bicycle is not a proper substitute for this `Bike` abstraction.**

---

# ✅ Better Design

The problem is the abstraction.

Not every bike has an engine.

So instead of putting `startEngine()` inside the common `Bike` abstraction, we should put only behavior that **all bikes can perform**.

```java
interface Bike {

    void ride();
}
```

Now `MotorBike`:

```java
class MotorBike implements Bike {

    @Override
    public void ride() {
        System.out.println("MotorBike is riding");
    }

    public void startEngine() {
        System.out.println("MotorBike engine started");
    }
}
```

And `Bicycle`:

```java
class Bicycle implements Bike {

    @Override
    public void ride() {
        System.out.println("Bicycle is riding");
    }
}
```

Now the relationship makes sense:

```text
             Bike
            /    \
           /      \
    MotorBike   Bicycle
```

Both support:

```java
ride()
```

---

# Now Substitution Works

We can write:

```java
class BikeService {

    public static void startRide(Bike bike) {
        bike.ride();
    }
}
```

Now we can pass either implementation:

```java
Bike bike1 = new MotorBike();
Bike bike2 = new Bicycle();

BikeService.startRide(bike1);
BikeService.startRide(bike2);
```

Output:

```text
MotorBike is riding
Bicycle is riding
```

Both objects work correctly.

Therefore, both can be substituted for `Bike`.

---

# The Important Lesson

The problem was **not inheritance itself**.

The problem was an incorrect abstraction.

We originally had:

```text
Bike
 ↓
startEngine()
```

But a `Bicycle` doesn't have an engine.

So `Bicycle` could not satisfy the expectations created by `Bike`.

Instead, we identify the behavior that both actually share:

```text
Bike
 ↓
ride()
```

Now both `MotorBike` and `Bicycle` can correctly implement the abstraction.

---

# How to Identify an LSP Violation

Ask yourself:

> **"If I replace the parent object with the child object, will the program still behave correctly?"**

For example:

```java
Bike bike = new MotorBike();
```

works.

But:

```java
Bike bike = new Bicycle();
```

breaks the behavior because `Bicycle` cannot support `startEngine()`.

This indicates that the inheritance relationship or abstraction is wrong.

---

# SRP vs OCP vs LSP

These principles solve different problems.

### SRP

> **Does a class have one responsibility?**

```text
Invoice
   ↓
Calculate invoice

InvoicePrinter
   ↓
Print invoice
```

### OCP

> **Can we add new behavior without modifying existing code?**

```text
       InvoiceRepository
          /     |      \
       MySQL  MongoDB  PostgreSQL
```

### LSP

> **Can a child object safely replace its parent object?**

```text
          Bike
         /    \
        /      \
 MotorBike   Bicycle

Both can safely perform:
        ↓
       ride()
```

---

# Remember

### LSP in one line:

> **A subclass should be able to replace its parent class without breaking the expected behavior of the program.**

### Simple mental model:

```text
❌ Bad

Parent promises something
        ↓
Child cannot fulfill it
        ↓
Program breaks


✅ Good

Parent defines a valid contract
        ↓
Child fulfills that contract
        ↓
Child can safely replace parent
```

**Liskov Substitution Principle = Subtypes must be safely substitutable for their base types.**
