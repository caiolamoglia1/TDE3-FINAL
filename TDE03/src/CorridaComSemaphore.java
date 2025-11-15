// Versão correta com Semaphore binário justo 
import java.util.concurrent.*;

public class CorridaComSemaphore {
    // Contador compartilhado protegido por semáforo
    static int count = 0;
    
    // Semáforo binário com política justa (FIFO)
    static final Semaphore sem = new Semaphore(1, true);
    
    public static void main(String[] args) throws Exception {
        // Parâmetros: 8 threads e 250 mil operações por thread
        int T = 8;
        int M = 250_000;
        
        ExecutorService pool = Executors.newFixedThreadPool(T);
        
        // Tarefa sincronizada com semáforo
        Runnable r = () -> {
            for (int i = 0; i < M; i++) {
                try {
                    // Adquire permissão antes de acessar seção crítica
                    sem.acquire();
                    count++;
                } catch (InterruptedException e) {
                    // Preserva o status de interrupção
                    Thread.currentThread().interrupt();
                } finally {
                    // Garante liberação do semáforo
                    sem.release();
                }
            }
        };
        
        long t0 = System.nanoTime();
        
        // Distribui tarefas entre as threads
        for (int i = 0; i < T; i++) {
            pool.submit(r);
        }
        
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);
        
        long t1 = System.nanoTime();
        
        // Resultado correto esperado com sincronização
        System.out.printf("Esperado=%d, Obtido=%d, Tempo=%.3fs%n",
                T * M, count, (t1 - t0) / 1e9);
    }
}