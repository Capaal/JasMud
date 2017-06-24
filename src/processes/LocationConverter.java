package processes;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class LocationConverter implements Converter {
	
	public LocationConverter(GameState gameState) {
		super();
	}

	@Override
	public boolean canConvert(Class clazz) {
		return Location.class.equals(clazz);
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		Location location = (Location) value;
		writer.setValue(String.valueOf(location.getId()));
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		int id = Integer.parseInt(reader.getValue());
		Location location = WorldServer.getGameState().viewLocations().get(id);
		return location;
	}

}
