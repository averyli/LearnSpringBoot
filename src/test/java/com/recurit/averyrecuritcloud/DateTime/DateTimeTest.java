package com.recurit.averyrecuritcloud.DateTime;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

public class DateTimeTest {
    
    @Test
    public  void testDate()
    {
        StringBuilder stringBuilder=new StringBuilder();
        DateTime dateTime=DateTime.now();
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat sdf1=new SimpleDateFormat("hh:mmaz",Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone(""));
        try{
            Date date=sdf.parse("19:00");
            stringBuilder.append(sdf1.format(date));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        System.out.println(TimeZone.getDefault().getID());
        System.out.println(TimeZone.getDefault().getDisplayName());
        String p=TimeZone.getTimeZone("America/Los_Angeles").getDisplayName(true,TimeZone.SHORT,Locale.ENGLISH);
        stringBuilder.append("(")
            .append(p)
            .append(")");
        System.out.println(stringBuilder.toString());
    }
    
    @Test
    public void ArrayQueueTest()
    {
        //初始化时构造一个只有16个元素的双端队列,不允许存放null值
        ArrayDeque<String> arrayDeque=new ArrayDeque<>();
        
        arrayDeque.offer("www");
        arrayDeque.add("ssss");
        arrayDeque.push("pppp");
        for (String str:arrayDeque
        ) {
            System.out.println(arrayDeque.getFirst());
        }
        
        ArrayDeque<String> arrayDeque1=new ArrayDeque<>(32);
       
       
       
       arrayDeque1.add("add tail");
       arrayDeque1.addFirst("add front");
       arrayDeque1.add("last");
        Iterator<String> it=arrayDeque1.iterator();
       while(it.hasNext())
       {
           System.out.println(it.next());
       }
       
       Iterator<String> stringIterator = arrayDeque1.clone().descendingIterator();
       
       while(stringIterator.hasNext())
       {
           System.out.println(stringIterator.next());
       }
       arrayDeque.peek();
       arrayDeque.poll();
 
        arrayDeque.remove();//无元素时报异常
        arrayDeque.remove();//无元素时报异常
        arrayDeque.pop();//无元素时报异常
        arrayDeque.toArray();
    }
    
    //ArrayList允许存放null值,初始化容量为10个元素大小
    @Test
    public void testArrayList()
    {
        ArrayList<String> arrayList=new ArrayList<>(10);
        
        arrayList.add("sss0");
        arrayList.add("oo");
        arrayList.add("wwww");
        
        arrayList.trimToSize();
        for (String s:arrayList
        ) {
            System.out.println("Number = " + s);
        }
        System.out.println("Number = " + arrayList.size());
        System.out.println(arrayList);
        
        String[] a=new String[]{"ww","qq","ee"};
        List list= Arrays.asList(a);
        System.out.println(list);
        Collections.sort(list);
        System.out.println(list);
    }
    
    @Test
    public void testBitSet()
    {
        DateTime dateTime=DateTime.now();
        
        BitSet bitSet=new BitSet();
        bitSet.set(10);
        bitSet.set(0,true);
        
        BitSet bitSet1=new BitSet();
        
    }
    
    @Test
    public void testUUId()
    {
        System.out.println(UUID.randomUUID().toString());
        
    }
}
