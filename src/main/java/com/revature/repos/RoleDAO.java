package com.revature.repos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import com.revature.models.Role;
import com.revature.util.ConnectionUtil;

public class RoleDAO implements IRoleDAO{

	public Role findByRoleId(int roleId) {
		try (Connection conn = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM user_role WHERE role_id = '" + roleId + "';";

			Statement statement = conn.createStatement();

			ResultSet result = statement.executeQuery(sql);

			if(result.next()) {
				Role r = new Role();
				r.setRole(result.getString("u_role"));

				return r;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}