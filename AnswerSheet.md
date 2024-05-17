# Theoretical Questions' Answer Sheet

## Question 1

**What will be printed after interrupting the thread?**

```java
public static class SleepThread extends Thread {
        public void run() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("Thread was interrupted!");
            } finally {
                System.out.println("Thread will be finished here!!!");
            }
        }
    }

    public static void main(String[] args) {
        SleepThread thread = new SleepThread();
        thread.start();
        thread.interrupt();
    }
```

## Answer

* In the main() method we start the thread and after creating a new thread, the run() method will be invoked.
* Inside the try block we sleep threads for 10 seconds that is a long time for these simple commands.
* Meanwhile, we interrupt the thread in the main() method that throws an InterruptedException.
* That moment the process in the try block is stopped and the catch block is being executed.
* Regardless of facing an exception the finally block executes, and we have the following output.

**Output**

```
Thread was interrupted!
Thread will be finished here!!!
```


## Question 2

In Java, what would be the outcome if the `run()` method of a `Runnable` object is invoked directly, without initiating it inside a `Thread` object?
```java
public class DirectRunnable implements Runnable {
    public void run() {
        System.out.println("Running in: " + Thread.currentThread().getName());
    }
}

public class Main {
    public static void main(String[] args) {
        DirectRunnable runnable = new DirectRunnable();
        runnable.run();
    }
}
```
## Answer
**The code within the run() method will run in the same thread of execution, rather than in a new thread.**


## Question 3

Elaborate on the sequence of events that occur when the `join()` method of a thread (let's call it `Thread_0`) is invoked within the `Main()` method of a Java program.
```java
public class JoinThread extends Thread {
    public void run() {
        System.out.println("Running in: " + Thread.currentThread().getName());
    }
}

public class Main {
    public static void main(String[] args) {
        JoinThread thread = new JoinThread();
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Back to: " + Thread.currentThread().getName());
    }
}
```
## Answer

* When we invoke the start() method of Thread_0 object (thread), it creates a new thread and invoke the run() method of the object.
* In the try block we call join() method of Thread_0 and the calling thread goes into a waiting state. It remains in a waiting state until the referenced thread terminates.
* The join() method may also return if the referenced thread is interrupted. In this case, the method throws an InterruptedException.