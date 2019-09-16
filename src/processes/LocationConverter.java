package processes;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

// Am xstream converter to handle Locations.
public class LocationConverter implements Converter {
	
	public LocationConverter() {
		super();
	}

	// This can only convert Locations.
	@Override
	public boolean canConvert(Class clazz) {
		return Location.class.equals(clazz);
	}

	// Simplifies a Location down to just its ID and designation as being a Location class.
	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		Location location = (Location) value;
		writer.setValue(String.valueOf(location.getId()));
	}

	// When loaded, converts the Location's ID into the actual Location.
	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		int id = Integer.parseInt(reader.getValue());
		Location location = WorldServer.getGameState().viewLocations().get(id);
		return location;
	}

}
