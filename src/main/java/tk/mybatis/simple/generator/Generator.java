package tk.mybatis.simple.generator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * MyBatis代码生成器工具类<br/>
 * <ol>优点：
 * <li>generatorConfig.xml配置的一些特殊的类（如commentGenerator标签中type属性配置的时CommentGenerator类）只要在当前项目中，
 * 或者在当前项目的classpath中， 就可以直接使用。</li>
 * <li><b>问题最少，配置最容易</b>，因此推荐使用。</li></ol>
 * <ol>缺点：
 * <li>和当前项目是绑定在一起的，在Maven 多子模块的情况下，可能需要<b>增加编写代码量和配置量，配置多个，管理不方便。</b></li></ol>
 * @author Administrator
 *
 */
public class Generator {
	public static void main(String[] args) {
		try {
			generate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void generate() throws Exception {
		//MBG执行过程中的警告信息
		List<String> warnings = new ArrayList<String>();
		
		//生成的代码重复是是否覆盖原代码
		boolean overwrite = true;
		
		//读取MBG配置文件
		InputStream is = Generator.class.getResourceAsStream("/generator/generatorConfig.xml");
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(is);
		is.close();
		
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		
		//创建MBG
		MyBatisGenerator mbg = new MyBatisGenerator(config, callback, warnings);
		
		//执行生成代码
		mbg.generate(null);
		
		//输出警告信息
		for (String string : warnings) {
			System.err.println("warning : " + string);
		}
	}
}











