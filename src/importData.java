import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class importData {
    public static void main(String args[]) throws InterruptedException {
        long starTime = System.currentTimeMillis();
        writerFast();
        long endTime = System.currentTimeMillis();
        System.out.println("写文件需要 " + (endTime - starTime) + "ms");
        //  reader();
    }

    public static void reader() {
        File f = new File("e:/txl/data/data.txt");

        Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String temp;
            long starTime = System.currentTimeMillis();
            while ((temp = br.readLine()) != null) {
                int num = Integer.valueOf(temp);
                //refactor
                map.merge(num, 1, (a, b) -> a + b);
            }

            long endTime = System.currentTimeMillis();

            System.out.println("读文件需要 " + (endTime - starTime) + "ms");

            long startime2 = System.currentTimeMillis();
            // refactor
            Map.Entry<Integer, Integer> item = map.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get();
            Integer flagKey = item.getKey();
            Integer flagValue = item.getValue();
            long endtime2 = System.currentTimeMillis();

            System.out.println("排序需要 " + (endtime2 - startime2) + "ms");
            System.out.println("maxkey :" + flagKey + " flagvalue :" + flagValue);

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
            for (int j = 0; j < 100000000; j++) {
                int num = Math.round(random.nextFloat() * 1000.0f);
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
