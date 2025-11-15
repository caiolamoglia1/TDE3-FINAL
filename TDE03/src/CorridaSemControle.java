// Versão com condição de corrida (sem sincronização) 
import java.util.concurrent.*;

public class CorridaSemControle {
    // Variável compartilhada entre threads - vulnerável a race condition
    static int count = 0;
    
    public static void main(String[] args) throws Exception {
        // Configuração: 8 threads, cada uma executa 250 mil iterações
        int T = 8;
        int M = 250_000;
        
        // Pool de threads para gerenciar a execução concorrente
        ExecutorService pool = Executors.newFixedThreadPool(T);
        
        // Tarefa que será executada por cada thread
        Runnable r = () -> {
            // Incremento sem proteção - operação não atômica
            for (int i = 0; i < M; i++) {
                count++;
            }
        };
        
        // Marca o tempo inicial
        long t0 = System.nanoTime();
        
        // Submete as tarefas ao pool
        for (int i = 0; i < T; i++) {
            pool.submit(r);
        }
        
        // Encerra o pool e aguarda conclusão
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);
        
        // Marca o tempo final
        long t1 = System.nanoTime();
        
        // Exibe o resultado
        System.out.printf("Esperado=%d, Obtido=%d, Tempo=%.3fs%n",
                T * M, count, (t1 - t0) / 1e9);
    }
} 