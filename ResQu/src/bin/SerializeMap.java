package bin;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SerializeMap {

	@SuppressWarnings("rawtypes")
	public static void deSerialize (){
		{
			HashMap<Integer, String> map = null;
			try
			{
				FileInputStream fis = new FileInputStream("hashmap.ser");
				ObjectInputStream ois = new ObjectInputStream(fis);
				map = (HashMap) ois.readObject();
				ois.close();
				fis.close();
			}catch(IOException ioe)
			{
				ioe.printStackTrace();

			}catch(ClassNotFoundException c)
			{
				c.printStackTrace();
			}
			System.out.println("Deserialized HashMap..");
			Set set = map.entrySet();
			Iterator iterator = set.iterator();
			while(iterator.hasNext()) {
				Map.Entry mentry = (Map.Entry)iterator.next();
				System.out.print("key: "+ mentry.getKey() + " & Value: ");
				System.out.println(mentry.getValue());
			}
		}
	}
	
	public static void main(String [] args)
	{
		HashMap<Integer, String> hmap = new HashMap<Integer, String>();
		hmap.put(11, "AB");
		hmap.put(2, "CD");
		hmap.put(33, "EF");
		hmap.put(9, "GH");
		hmap.put(3, "IJ");
		try
		{
			FileOutputStream fos =	new FileOutputStream("hashmap.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(hmap);
			oos.close();
			fos.close();
			System.out.printf("Serialized HashMap data is saved in hashmap.ser");
		}catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		deSerialize();
		
//		String test = "Sensitivity (Antimicrobial susceptibility) [Finding]";
//		//String [] list =  test.split("[");
//		String list =  test.replaceAll("\\(.*\\)", "").replaceAll("\\[.*\\]","");
//		System.out.println(list);//+list[1]);
	}

}
