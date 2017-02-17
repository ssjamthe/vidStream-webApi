package com.appify.vidstream.newWebApi.data.jdbc;

import com.appify.vidstream.newWebApi.data.*;
import com.appify.vidstream.newWebApi.util.WebAPIUtil;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by swapnil on 24/11/16.
 */
public class JDBCCategoryDataLoader {

	private DataSource dataSource;
	private JDBCOrderedVideosDataLoader videoDataLoader;
	private WebAPIUtil webAPIUtil;

	@Inject
	JDBCCategoryDataLoader(DataSource dataSource, JDBCOrderedVideosDataLoader videoDataLoader, WebAPIUtil webAPIUtil) {

		this.dataSource = dataSource;
		this.videoDataLoader = videoDataLoader;
		this.webAPIUtil = webAPIUtil;
	}

	public List<Category> getCategoriesForCategorization(String categorizationId) {

		try (Connection con = dataSource.getConnection();) {
			List<Category> categories = new ArrayList<Category>();

			// Getting top level categories.
			String sql = "select id,name,image from category where categorization_id='" + categorizationId
					+ "' and id not in (select child_category_id from " + "parent_child_category_mappings)";

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String id = rs.getString("id");
				String name = rs.getString("name");
				String image = rs.getString("image");

				Category category = new Category();
				category.setId(id);
				category.setName(name);
				category.setImageURL(webAPIUtil.getImageURL(image));
				setChildren(category);

				categories.add(category);
			}

			return categories;
		} catch (SQLException ex) {
			throw new RuntimeException("Problem getting categories data.", ex);
		}

	}

	private void setChildren(Category parentCategory) {
		try (Connection con = dataSource.getConnection();) {

			Queue<Category> parentQueue = new LinkedList<>();
			parentQueue.offer(parentCategory);

			while (!parentQueue.isEmpty()) {

				Category currParent = parentQueue.poll();

				String sql = "select id,name,image from category where id in (select child_category_id "
						+ "from parent_child_category_mappings where parent_category_id='" + currParent.getId() + "')";

				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql);

				ImmutableList.Builder<Entity> childCategoriesBuilder = ImmutableList.builder();

				while (rs.next()) {
					int id = rs.getInt("id");
					String name = rs.getString("name");
					String image = rs.getString("image");

					Category category = new Category();
					category.setId(Integer.toString(id));
					category.setName(name);
					category.setImageURL(webAPIUtil.getImageURL(image));

					childCategoriesBuilder.add(category);

					parentQueue.offer(category);
				}

				ImmutableList<Entity> childCategories = childCategoriesBuilder.build();

				if (childCategories.isEmpty()) {
					List<OrderedVideos> videos = videoDataLoader.getOrderedVideosForCategory(currParent.getId());
					currParent.setChildType(EntityType.ORDERED_VIDEOS);
					currParent.setChildren(
							ImmutableList.copyOf(videos.stream().map(v -> (Entity) v).collect(Collectors.toList())));

				} else {
					currParent.setChildType(EntityType.CATEGORY);
					currParent.setChildren(childCategories);
				}
			}

		} catch (SQLException ex) {
			throw new RuntimeException("Problem getting categories data.", ex);
		}
	}
}
