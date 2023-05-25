package com.crud.dao;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.crude.model.User;

public class UserDAO {
	
	public UserDAO() {
	}
	
	 private String URL = "jdbc:mysql://localhost:3306/crudapp?useSSL=false";
	 private String userName = "root";
	 private String password = "savan";
	 
	 private static final String InserUser = "INSERT INTO users" + "  (name, email, country) VALUES "
				+ " (?, ?, ?);";

		private static final String selectUserById = "select id,name,email,country from users where id =?";
		private static final String selectAllUsers = "select * from users";
		private static final String deleteUser = "delete from users where id = ?;";
		
		private static final String updateUser = "update users set name = ?,email= ?, country =? where id = ?;";
		
		protected Connection getConnection() {
			Connection conn = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(URL, userName, password);
			} catch (SQLException e) {
			
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
			}
			return conn;
		}
				
				public void insertUser(User user) throws SQLException {
					System.out.println(InserUser);
					// try-with-resource statement will auto close the connection.
					try (Connection conn = getConnection();
							PreparedStatement preparedStatement = conn.prepareStatement(InserUser)) {
						preparedStatement.setString(1, user.getName());
						preparedStatement.setString(2, user.getEmail());
						preparedStatement.setString(3, user.getCountry());
						System.out.println(preparedStatement);
						preparedStatement.executeUpdate();
					} catch (SQLException e) {
						printSQLException(e);
					}
				}

				public User selectUser(int id) {
					User user = null;
				
					try (Connection conn = getConnection();
			
							PreparedStatement preparedStatement = conn.prepareStatement(selectUserById);) {
						preparedStatement.setInt(1, id);
						System.out.println(preparedStatement);
					
						ResultSet rs = preparedStatement.executeQuery();

						
						while (rs.next()) {
							String name = rs.getString("name");
							String email = rs.getString("email");
							String country = rs.getString("country");
							user = new User(id, name, email, country);
						}
					} catch (SQLException e) {
						printSQLException(e);
					}
					return user;
				}

				public List<User> selectAllUsers() {

				
					List<User> users = new ArrayList<>();
					
					try (Connection conn = getConnection();

						PreparedStatement preparedStatement = conn.prepareStatement(selectAllUsers);) {
						System.out.println(preparedStatement);
				
						ResultSet rs = preparedStatement.executeQuery();

						while (rs.next()) {
							int id = rs.getInt("id");
							String name = rs.getString("name");
							String email = rs.getString("email");
							String country = rs.getString("country");
							users.add(new User(id, name, email, country));
						}
					} catch (SQLException e) {
						printSQLException(e);
					}
					return users;
				}

				public boolean deleteUser(int id) throws SQLException {
					boolean rowDeleted;
					try (Connection conn = getConnection();
							PreparedStatement statement = conn.prepareStatement(deleteUser);) {
						statement.setInt(1, id);
						rowDeleted = statement.executeUpdate() > 0;
					}
					return rowDeleted;
				}

				public boolean updateUser(User user) throws SQLException {
					boolean rowUpdated;
					try (Connection conn = getConnection();
							PreparedStatement statement = conn.prepareStatement(updateUser);) {
						statement.setString(1, user.getName());
						statement.setString(2, user.getEmail());
						statement.setString(3, user.getCountry());
						statement.setInt(4, user.getId());

						rowUpdated = statement.executeUpdate() > 0;
					}
					return rowUpdated;
				}

				private void printSQLException(SQLException ex) {
					for (Throwable e : ex) {
						if (e instanceof SQLException) {
							e.printStackTrace(System.err);
							System.err.println("SQLState: " + ((SQLException) e).getSQLState());
							System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
							System.err.println("Message: " + e.getMessage());
							Throwable t = ex.getCause();
							while (t != null) {
								System.out.println("Cause: " + t);
								t = t.getCause();
							}
						}
					}
				}

			
}


