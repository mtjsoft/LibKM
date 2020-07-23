package com.mtjsoft.www.kotlinmvputils.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 一个简单的Adapter，继承自BaseAdapter<br/>
 * 默认实现了出getView方法意外的其他的方法
 * @author mtj
 *
 * @param <T>
 */
public abstract class HHBaseAdapter<T> extends BaseAdapter
{

	private Context mContext;
	private List<T> mList;
	public HHBaseAdapter(Context context,List<T> list)
	{
		this.mContext=context;
		this.mList=list;
	}
	/**
	 * 返回构造HHBaseAdapter的时候传入的Context对象
	 * @return
	 */
	protected Context getContext()
	{
		return mContext;
	}
	/**
	 * 返回adapter绑定的数据源
	 * @return
	 */
	public List<T> getList()
	{
		return mList;
	}
	@Override
	public int getCount()
	{
		return mList==null?0:mList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mList==null?null:mList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}


}
