/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-24 下午2:31:28
 */
package com.absir.context.schedule.cron;

import java.util.Date;

import com.absir.context.schedule.ScheduleRunableAbstract;

/**
 * @author absir
 * 
 */
public class CronFixDelayRunable extends ScheduleRunableAbstract {

	/** fixDelay */
	private long fixDelay;

	/**
	 * @param runnable
	 * @param fixDelay
	 */
	public CronFixDelayRunable(Runnable runnable, long fixDelay) {
		super(runnable);
		this.fixDelay = fixDelay;
	}

	/**
	 * @return the fixDelay
	 */
	public long getFixDelay() {
		return fixDelay;
	}

	/**
	 * @param fixDelay
	 *            the fixDelay to set
	 */
	public void setFixDelay(long fixDelay) {
		this.fixDelay = fixDelay;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.context.schedule.ScheduleRunable#start(java.util.Date)
	 */
	@Override
	public void start(Date date) {
		// TODO Auto-generated method stub
		scheduleTime += date.getTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.context.schedule.ScheduleRunable#getNextScheduleTime(java.util
	 * .Date)
	 */
	@Override
	public long getNextScheduleTime(Date date) {
		// TODO Auto-generated method stub
		if (fixDelay <= 0) {
			return 0;
		}

		long minTime = date.getTime();
		while (scheduleTime < minTime) {
			scheduleTime += fixDelay;
		}

		return scheduleTime;
	}

	/**
	 * @param fixDelay
	 * @return
	 */
	public CronFixDelayRunable transformCronFixDelayRunable(long fixDelay) {
		this.fixDelay = 0;
		return new CronFixDelayRunable(runnable, fixDelay);
	}
}
