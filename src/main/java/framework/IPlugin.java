package framework;

import java.util.List;



public interface IPlugin {

	@SuppressWarnings("unchecked")
	public static List<IPlugin> INSTANCE = (List<IPlugin>)Factory.getPlugin(IPlugin.class);
	public String getName();

}


