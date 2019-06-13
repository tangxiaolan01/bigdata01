import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class importData {
    public  static  HashMap<Integer,Integer>  maxMap = new HashMap<>();
    public static void main(String args[]) throws InterruptedException {
/*        long starTime = System.currentTimeMillis();
        writerFast();
        long endTime = System.currentTimeMillis();
        System.out.println("写文件需要 " + (endTime - starTime) + "ms");*/

        long starTime = System.currentTimeMillis();
          readerFast();
        long endTime = System.currentTimeMillis();
        System.out.println("读文件需要 " + (endTime - starTime) + "ms");
        Map.Entry<Integer, Integer> item = maxMap.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get();
        System.out.println("maxvalue is: "+ item.getKey() + "max times is :" + item.getValue());

       /* for(int key:maxMap.keySet())
        {
            System.out.println("Key: "+key+" Value: "+maxMap.get(key));
        }*/

    }

    public static void readerFast() throws InterruptedException {
  /*      for(int i = 0 ;i < 10 ;i++){
            reader(i);
        }*/
/*
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    reader(index,maxMap);
                }
            });

        }
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.HOURS);
*/


        List<Integer> arr = new ArrayList<>(10);
        for ( int i = 0;i< 10 ;i++){
            arr.add(i);
        }
        arr.parallelStream().forEach( index->reader(index,maxMap));

        System.out.println("write over");
    }



    public static void reader(Object index, HashMap<Integer,Integer> mapMax) {
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

            Map.Entry<Integer, Integer> item = map.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get();
            Integer flagKey = item.getKey();
            Integer flagValue = item.getValue();
            mapMax.put(item.getKey(),item.getValue());
           /* long startime2 = System.currentTimeMillis();
            // refactor
            Map.Entry<Integer, Integer> item = map.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get();
            Integer flagKey = item.getKey();
            Integer flagValue = item.getValue();
            long endtime2 = System.currentTimeMillis();

            System.out.println("排序需要 " + (endtime2 - startime2) + "ms");
            System.out.println("maxkey :" + flagKey + " flagvalue :" + flagValue);
*/
        } catch (IOException e) {
            e.printStackTrace();
        }
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
