import java.util.concurrent.*;

public class DeadlockFixed {
    // Recursos com hierarquia implícita: A deve ser adquirido antes de B
    static final Object LOCK_A = new Object();
    static final Object LOCK_B = new Object();

    public static void main(String[] args) {
        // Thread 1: segue a ordem global A -> B
        Thread t1 = new Thread(() -> {
            System.out.println("T1: Esperando LOCK_A");
            
            synchronized (LOCK_A) {
                System.out.println("T1: Peguei a LOCK_A");
                dormir(50);
                
                System.out.println("T1: Esperando LOCK_B");
                synchronized (LOCK_B) {
                    System.out.println("T1: Peguei a LOCK_B");
                    System.out.println("T1 concluiu");
                }
            }
        });

        // Thread 2: também segue a ordem global A -> B (corrigido)
        Thread t2 = new Thread(() -> {
            System.out.println("T2: Esperando LOCK_A");
            
            synchronized (LOCK_A) {
                System.out.println("T2: Peguei a LOCK_A");
                dormir(50);
                
                System.out.println("T2: Esperando LOCK_B");
                synchronized (LOCK_B) {
                    System.out.println("T2: Peguei a LOCK_B");
                    System.out.println("T2 concluiu");
                }
            }
        });

        // Execução das threads sem deadlock
        t1.start();
        t2.start();
    }

    // Simulação de trabalho computacional
    static void dormir(long ms) {
        try { 
            Thread.sleep(ms); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}