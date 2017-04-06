package com.appify.vidstream.newWebApi.data;

import com.appify.vidstream.newWebApi.data.jdbc.JDBCPropertyDataLoader;
import com.google.common.util.concurrent.AbstractScheduledService;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by swapnil on 01/12/16.
 */
@Singleton
public class CombinedPropertyDataLoader extends PropertyDataLoader {

	private JDBCPropertyDataLoader jdbcPropertyDataLoader;
	private FilePropertyDataLoader filePropertyDataLoader;
	private volatile Map<String, String> props = new HashMap<>();

	@Inject
	public CombinedPropertyDataLoader(JDBCPropertyDataLoader jdbcPropertyDataLoader,
			FilePropertyDataLoader filePropertyDataLoader) {

		this.jdbcPropertyDataLoader = jdbcPropertyDataLoader;
		this.filePropertyDataLoader = filePropertyDataLoader;

	}

	@Override
	public Map<String, String> getProps() {
		return this.props;
	}

	private void loadData() {
		Map<String, String> props = new HashMap<>();
		// Database property takes precedence in case of clash.
		props.putAll(filePropertyDataLoader.getProps());
		props.putAll(jdbcPropertyDataLoader.getProps());
		this.props = props;
	}

	@Override
	protected void work() {
		loadData();
	}

	@Override
	protected void startUp() {
		loadData();
	}

	@Override
	protected Scheduler scheduler() {
		return Scheduler.newFixedDelaySchedule(0, 5, TimeUnit.MINUTES);
	}
}
