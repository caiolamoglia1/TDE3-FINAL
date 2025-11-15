import java.util.concurrent.*;

public class DeadlockDemo {
    // Recursos compartilhados que serão disputados
    static final Object LOCK_A = new Object();
    static final Object LOCK_B = new Object();

    public static void main(String[] args) {
        // Thread 1: tenta adquirir na ordem A -> B
        Thread t1 = new Thread(() -> {
            synchronized (LOCK_A) {
                System.out.println("Thread 1 adquiriu LOCK_A");
                dormir(50);
                
                synchronized (LOCK_B) {
                    System.out.println("T1 concluiu");
                }
            }
        });

        // Thread 2: tenta adquirir na ordem B -> A (inversa)
        Thread t2 = new Thread(() -> {
            synchronized (LOCK_B) {
                System.out.println("Thread 2 adquiriu LOCK_B");
                dormir(50);
                
                synchronized (LOCK_A) {
                    System.out.println("T2 concluiu");
                }
            }
        });

        // Inicia ambas as threads simultaneamente
        t1.start();
        t2.start();
    }

    // Método auxiliar para simular processamento
    static void dormir(long ms) {
        try { 
            Thread.sleep(ms); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}