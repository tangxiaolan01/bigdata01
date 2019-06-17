import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class importData {
    public static HashMap<Integer,Integer>  maxMap = new HashMap<>();
    public static void main(String args[]) throws InterruptedException {
     /*   long starTime = System.currentTimeMillis();
        writerFast();
        long endTime = System.currentTimeMillis();
        System.out.println("写文件需要 " + (endTime - starTime) + "ms");*/

        long starTime = System.currentTimeMillis();
          readerFast();
        long endTime = System.currentTimeMillis();
        System.out.println("读文件需要 " + (endTime - starTime) + "ms");



    }

    public static void readerFast() throws InterruptedException {

        Map.Entry<Integer,Integer> result = IntStream.range(0,3).parallel().mapToObj(importData::reader)
                .reduce(new HashMap<Integer, Integer>(),(sum,element)->{
                    element.forEach((k,v) ->sum.merge(k,v,(x,y)->x+y));
                    return sum;
                },(sum, element) -> {
                    HashMap<Integer, Integer> newMap = new HashMap<>(sum);
                    element.forEach((k, v) -> newMap.merge(k, v, (x, y) -> x + y));
                    return newMap;
                }).entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get();

        System.out.println(result.getKey() +  "   "+result.getValue() + " result is  " + result.toString());
        System.out.println("write over");
    }




    public static Map<Integer,Integer> reader(Object index ) {
        int i = (int)index;
        File f = new File("e:/txl/data/data_"+i  +".txt");

        Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String temp;

            while ((temp = br.readLine()) != null) {
                int num = Integer.valueOf(temp);
                //refactor
                map.merge(num, 1, (a, b) -> a + b);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static void writer(int i) {
        Random random = new Random();

        File f = new File("e:/txl/data/data_" + i + ".txt");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)))) {
            for (int j = 0; j < 2000000; j++) {
                int num = random.nextInt(Integer.MAX_VALUE);
                bw.write(Integer.toString(num));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writerFast() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    writer(index);
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.HOURS);

        System.out.println("write over");
    }

}
