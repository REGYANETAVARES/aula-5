// Arquivo base de leitura, execução e medição de desempenho dos algoritmos
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Ordenacao500K {
    static long comparacoes = 0;
    static long movimentacoes = 0;

    public static void main(String[] args) throws IOException {
        int[] dados = lerArquivo("dados500mil.txt");

        // Quick Sort
        int[] copiaQS = Arrays.copyOf(dados, dados.length);
        comparacoes = 0;
        movimentacoes = 0;
        long inicio = System.nanoTime();
        quickSort(copiaQS, 0, copiaQS.length - 1);
        long fim = System.nanoTime();
        salvarArquivo(copiaQS, "saida_quick.txt");
        gerarRelatorio("relatorio_quick.txt", "Quick Sort", fim - inicio);

        // Merge Sort
        int[] copiaMS = Arrays.copyOf(dados, dados.length);
        comparacoes = 0;
        movimentacoes = 0;
        inicio = System.nanoTime();
        mergeSort(copiaMS, 0, copiaMS.length - 1);
        fim = System.nanoTime();
        salvarArquivo(copiaMS, "saida_merge.txt");
        gerarRelatorio("relatorio_merge.txt", "Merge Sort", fim - inicio);

        // Radix Sort
        int[] copiaRS = Arrays.copyOf(dados, dados.length);
        comparacoes = 0;
        movimentacoes = 0;
        inicio = System.nanoTime();
        radixSort(copiaRS);
        fim = System.nanoTime();
        salvarArquivo(copiaRS, "saida_radix.txt");
        gerarRelatorio("relatorio_radix.txt", "Radix Sort", fim - inicio);
    }

    static int[] lerArquivo(String nome) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src/ordenacao/dados500mil.txt"));

        List<Integer> lista = new ArrayList<>();
        String linha;
        while ((linha = br.readLine()) != null) lista.add(Integer.parseInt(linha));
        br.close();
        return lista.stream().mapToInt(i -> i).toArray();
    }

    static void salvarArquivo(int[] vetor, String nome) throws IOException {
  BufferedWriter bw = new BufferedWriter(new FileWriter(nome));


        for (int v : vetor) bw.write(v + "\n");
        bw.close();
    }

    static void gerarRelatorio(String nome, String algoritmo, long tempoNano) throws IOException {
        long ms = tempoNano / 1_000_000;
        long s = ms / 1000;
        long m = s / 60;
        long h = m / 60;
        BufferedWriter bw = new BufferedWriter(new FileWriter(nome));
        bw.write("Nome do aluno: Regyane Tavares de Oliveira\n");
        bw.write("Nome do Algoritmo: " + algoritmo + "\n");
        bw.write(String.format("Tempo de Execucao: %02d:%02d:%02d:%03d\n", h % 60, m % 60, s % 60, ms % 1000));
        bw.write("Quantidade de comparacoes: " + comparacoes + "\n");
        bw.write("Quantidade de movimentacoes: " + movimentacoes + "\n");
        bw.close();
    }

    static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            comparacoes++;
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    static void mergeSort(int[] arr, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);
            merge(arr, l, m, r);
        }
    }

    static void merge(int[] arr, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;
        int[] L = new int[n1];
        int[] R = new int[n2];
        for (int i = 0; i < n1; i++) L[i] = arr[l + i];
        for (int j = 0; j < n2; j++) R[j] = arr[m + 1 + j];
        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            comparacoes++;
            if (L[i] <= R[j]) arr[k++] = L[i++];
            else arr[k++] = R[j++];
            movimentacoes++;
        }
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    static void radixSort(int[] arr) {
        int max = Arrays.stream(arr).max().orElse(0);
        for (int exp = 1; max / exp > 0; exp *= 10) countingSortByDigit(arr, exp);
    }

    static void countingSortByDigit(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10];
        for (int i = 0; i < n; i++) count[(arr[i] / exp) % 10]++;
        for (int i = 1; i < 10; i++) count[i] += count[i - 1];
        for (int i = n - 1; i >= 0; i--) {
            int idx = (arr[i] / exp) % 10;
            output[count[idx] - 1] = arr[i];
            count[idx]--;
            movimentacoes++;
        }
        for (int i = 0; i < n; i++) arr[i] = output[i];
    }

    static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        movimentacoes++;
    }
}
