import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class importData {
public static void  main(String args[]){
    long startime = System.currentTimeMillis();
    writerFast();
    long endtime = System.currentTimeMillis();
    System.out.println("写文件需要 " + (endtime-startime) + "ms");
  //  reader();

}
public static void reader(){

    File f = new File("e:/txl/data/data.txt");
    Map<Integer,Integer> map = new HashMap<Integer, Integer>();
    BufferedReader br = null;
    int key = 0;
    int value = 0;
    int flagKey = 0;
    int flagValue = 0;
    try {
        br = new BufferedReader(new FileReader(f));
        String temp ;
        long startime = System.currentTimeMillis();
        while ((temp = br.readLine())!= null){
           int num =  Integer.valueOf(temp);
            if(map.get(num)== null){
                map.put(num,0);
            }else {
                map.put(num,map.get(num)+1);
            }
        }

        long endtime = System.currentTimeMillis();

        System.out.println("读文件需要 " + (endtime-startime) + "ms");

        long startime2 = System.currentTimeMillis();
        Set<Map.Entry<Integer,Integer>> entrySet =  map.entrySet();
        for(Map.Entry<Integer,Integer> item :entrySet){
            key = item.getKey();
            value = item.getValue();

            if(flagValue < value){
                flagKey = key;
                flagValue = value;
            }
        }
        long endtime2 = System.currentTimeMillis();


        System.out.println("排序需要 " + (endtime2-startime2) + "ms");
        System.out.println("maxkey :" + flagKey +" flagvalue :" + flagValue);
    } catch (Exception e) {
        e.printStackTrace();
    }finally {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public static  void writer(int i) {
    Random random = new Random();
     File f = new File("e:/txl/data/data_"+ i+".txt");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedWriter bw = null;
        StringBuilder builder = new StringBuilder();
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
            for (int j = 0; j < 100000000; j++) {
                int num = Math.round(random.nextFloat() * 1000.0f);
                bw.write(Integer.toString(num));
                bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public  static  void writerFast(){
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(int i = 0;i<10;i++){
            final  int index = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    writer( index);
                }
            });
        }

    }

}
