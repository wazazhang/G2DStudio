package com.cell.rpg.scene;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import com.cell.rpg.NamedObject;
import com.cell.rpg.RPGFactory;
import com.cell.rpg.RPGObject;
import com.cell.rpg.display.SceneNode;
import com.g2d.annotation.Property;
//import com.cell.rpg.scene.script.trigger.Event;

public class Scene extends RPGObject implements NamedObject//, TriggersPackage
{
	transient private int 				int_id;

	public String						name;
	
	public SceneNode					scene_node;
	
	final public Vector<SceneUnit> 		scene_units = new Vector<SceneUnit>();
	transient private HashMap<String, SceneUnit> scene_unit_map = null;
//	private Triggers					triggers_package = new Triggers();
//	
//	private TriggerGenerator			binded_triggers = new TriggerGenerator();

//	------------------------------------------------------------------------------------------------------------------
	
	@Property("场景背景音乐")
	public String						bgm_sound_name;
	
	@Property("场景编组号码")
	public Integer						group;
	
	@Property("场景事件冷冻时间(毫秒)")
	public Integer						event_freezing_time_ms	= 5000;

	@Property("是否是副本")
	public boolean						is_instance_zone = false;
//	------------------------------------------------------------------------------------------------------------------
	
	public Scene(int id, String name) {
		super(id+"");
		this.int_id	= id;
		this.name	= name;
	}
	
	@Override
	protected void init_transient() {
		super.init_transient();
		if (getID() != null) {
			this.int_id = Integer.parseInt(getID());
		}
		if (group == null) {
			this.group = 0;
		}
//		if (triggers_package == null) {
//			triggers_package = new Triggers();
//		}
//		if (binded_triggers == null) {
//			binded_triggers = new TriggerGenerator();
//		}
	}
	
	final public int getIntID() {
		return int_id;
	}

	public void setID(RPGFactory f, String nid)
	{
		super.setID(f, nid);
		this.int_id = Integer.parseInt(nid);
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Class<?>[] getSubAbilityTypes() {
		SceneAbilityManager sam = SceneAbilityManager.getManager();
		if (sam != null) {
			Set<Class<?>> set = sam.getAllTypes();
			if (set != null) {
				return set.toArray(new Class<?>[set.size()]);
			}
		}
		return null;
	}

//	public Triggers getTriggersPackage() {
//		return triggers_package;
//	}
//
//	public TriggerGenerator getBindedTriggers() {
//		return binded_triggers;
//	}
	
//	------------------------------------------------------------------------------------------------------------------
	
	public SceneUnit getSceneUnit(String unit_edit_id) {
		synchronized (scene_units) {
			if (scene_unit_map == null) {
				scene_unit_map = new HashMap<String, SceneUnit>(scene_units.size());
				for (SceneUnit unit : scene_units) {
					scene_unit_map.put(unit.getID(), unit);
				}
			}
			return scene_unit_map.get(unit_edit_id);
		}
	}
}
