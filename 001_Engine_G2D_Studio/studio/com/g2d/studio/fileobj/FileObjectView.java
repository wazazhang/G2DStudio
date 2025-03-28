package com.g2d.studio.fileobj;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import com.cell.CUtil;
import com.cell.util.Pair;
import com.g2d.studio.Studio.ProgressForm;
import com.g2d.studio.gameedit.entity.IProgress;
import com.g2d.studio.io.File;
import com.g2d.studio.swing.G2DListItem;
import com.g2d.studio.swing.G2DTreeListView;
import com.g2d.studio.swing.G2DTreeNodeGroup;

@SuppressWarnings("serial")
public abstract class FileObjectView<T extends FileObject> extends G2DTreeListView<T> implements Comparator<G2DListItem>
{
	final String title;
	
	final protected File 		save_list_file;
	final protected File 		res_root;

	final private Map<File, T> nodes_file = new HashMap<File, T>();
	final private Map<String, T> nodes_name = new HashMap<String, T>();
	
	public FileObjectView(
			String title,
			ProgressForm progress, 
			File res_root, 
			File save_list_file, 
			String suffix)
	{
		super(new FileGroup<T>(title));
		this.save_list_file = save_list_file;
		this.res_root = res_root;		
		this.title = title;
		
		this.setComparator(this);
		
		HashMap<String, String> saved_list = readListFile();
		
		if (progress != null) progress.setMaximum(title, saved_list.size());
		int i = 0;
		for (Entry<String, String> e : saved_list.entrySet()) {
			File file = res_root.getChildFile(e.getKey() + suffix);
			if (file != null) {
				T item = createItem(file);
				if (item != null) {
					nodes_file.put(item.getFile(), item);
					nodes_name.put(item.getName(), item);
					String path = saved_list.get(item.getName());
					FileGroup<?> g = (FileGroup<?>)getRoot();
					if (path != null) {
						g = (FileGroup<?>)getRoot().loadPath(path);
					}
					g.addItem(item);
				}
			}
			if (progress != null) progress.increment();
			i++;
		}
		System.out.println("read : " + nodes_file.size());
		
//		this.getList().setVisibleRowCount(this.files.size()/10+1);
//		this.getList().setLayoutOrientation(JList.HORIZONTAL_WRAP);

		reload();
		
		getTree().expandAll();
	}

//	--------------------------------------------------------------------------------------------------------------
	
	synchronized public void refresh(IProgress progress)
	{
		Vector<T> 		added	= new Vector<T>();
		Vector<T> 		removed	= new Vector<T>(nodes_file.values());
		
		File 			files[]	= res_root.listFiles();
		
		if (progress != null) progress.setMaximum(title, files.length);
		int i = 0;
		for (File file : files) {
//			System.out.println(file);
			T node = this.nodes_file.get(file);
			if (node == null) {
				node = createItem(file);
				if (node != null) {
					added.add(node);
				}
			} else {
				removed.remove(node);
			}
			if (progress != null) progress.increment();
			i++;
		}
		
		System.out.println(title + " : add " + added.size());
		for (T item : added) {
			nodes_file.put(item.getFile(), item);
			nodes_name.put(item.getName(), item);
			getRoot().addItem(item);
		}
		
		System.out.println(title + " : remove " + removed.size());
		for (T item : removed) {
			nodes_file.remove(item.getFile());
			nodes_name.remove(item.getName());
			removeItem(item);
		}
	
		System.out.println(title + " : nodes_file " + nodes_file.size());
		System.out.println(title + " : nodes_name " + nodes_name.size());
		
	}

//	--------------------------------------------------------------------------------------------------------------
	
	abstract public T	createItem(File file);

	public int compare(G2DListItem o1, G2DListItem o2) {
		return o1.getListName().compareTo(o2.getListName());
	}
	
//	--------------------------------------------------------------------------------------------------------------
	
	public String saveAll()
	{
		StringBuilder new_list = new StringBuilder();
		for (Pair<T, NodeGroup<T>> p : getItemsPath()) {
			NodeGroup<T> g = p.getValue();
			T o = p.getKey();
			new_list.append(G2DTreeNodeGroup.toFullPathString(g, "/") + o.getName() + ";" + o.getSaveListArgs() + "\n");
		}
		save_list_file.writeUTF(new_list.toString());
		return new_list.toString();
	}
	
	public Vector<T> getNodes() {
		return new Vector<T>(nodes_name.values());
	}
	
	public T getNode(String node_name) {
		return nodes_name.get(node_name);
	}
	
	public int getNodeCount() {
		return nodes_name.size();
	}
	
	private HashMap<String, String> readListFile()
	{
		if (save_list_file.exists()) {
			String text = save_list_file.readUTF();
			String[] lines = CUtil.splitString(text, "\n");
			HashMap<String, String> ret = new HashMap<String, String>(lines.length);
			for (String line : lines) {
				try {
					int i = line.lastIndexOf(';');
					if (i > 0) {
						line = line.substring(0, i);
						int j = line.lastIndexOf("/");
						if (j > 0) {
							ret.put(line.substring(j+1), line.substring(0, j));
						} else {
							ret.put(line, null);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return ret;
		} else {
			return new HashMap<String, String>();
		}
	}
	
//	private HashMap<String, T> getItemsList() {
//		HashMap<String, T> ret = new LinkedHashMap<String, T>();
//		for (T i : getItems()) {
//			ret.put(i.getName(), i);
//		}
//		return ret;
//	}
	
//	--------------------------------------------------------------------------------------------------------------
	
	
}
