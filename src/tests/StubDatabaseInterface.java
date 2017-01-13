package tests;

import java.util.HashMap;

import interfaces.Container;
import interfaces.DatabaseInterface;
import interfaces.Mobile;

public class StubDatabaseInterface implements DatabaseInterface {
		
		public String enteredSQL;
		
		public void save(String sql) {
			this.enteredSQL = sql;
		}

		@Override
		public void connect(String username1, char[] password1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void loadLocations() {
			// TODO Auto-generated method stub
			
		}
	

		@Override
		public Mobile loadPlayer(String name, String password) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void saveAction(String sql) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Object viewData(String blockQuery, String column) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public HashMap<String, Object> returnBlockView(String blockQuery) {
			// TODO Auto-generated method stub
			return new HashMap<String, Object>();
		}

		@Override
		public void makeConnection() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void disconnect() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void loadMobs() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void loadSkillBooks() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void increaseSequencer() {
			// TODO Auto-generated method stub
			
		}
	}