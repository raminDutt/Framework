package framework;

import java.util.List;



public interface IPlugin {

	public static List<IPlugin> INSTANCE = (List<IPlugin>)Factory.getPlugin(IPlugin.class);
	public String getName();

}


