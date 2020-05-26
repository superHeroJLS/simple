package tk.mybatis.simple.util;

import java.net.URL;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class TestClass {
	public String name = "jls";
	
	static {
		System.out.println("this is static block");
	}
	
	public static void method() {
		System.out.println("this is static method");
	}
	
	public static void main(String[] args) {
//		拿ehcache缓存中的数据
//		String name = "D:" + File.separator + "Tmp_EhCache" + File.separator + "test.data";
//		
//		try {
//			ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(new File(name)));
//			net.sf.ehcache.Element ele = (Element) objInput.readObject();
//			Object key =  ele.getObjectKey();
//			Object value = ele.getObjectValue();
//			
//			System.out.println(key + "--" + value);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
//		往ehcache中手动put缓存数据
//		URL url = TestClass.class.getClassLoader().getResource("ehcache.xml");		
//		CacheManager cm = CacheManager.create(url);
//		Ehcache ehcache = cm.getEhcache("test");
//		ehcache.put(new Element("k", "v"));
//		ehcache.flush();
//		cm.shutdown();
		
		
		
		
		
		
	}
}
