package test;

import org.bson.types.ObjectId;

import esayhelper.DBHelper;
import interfaceApplication.WebGroup;

public class testwebs {
	private static WebGroup group = new WebGroup();

	public static void main(String[] args) throws InterruptedException {
		// 测试webgroup
		// System.out.println(group.WebGroupBatchDelete("58e6e5e61a4769cbf51e1425,58e6e51d1a4769cbf51e12cb"));
		DBHelper helper = new DBHelper("mongodb", "test","_id");
//		helper.or().eq("_id", new ObjectId("58e6e7b31a4769cbf51e1772")).eq("_id",
//				new ObjectId("58e6e6441a4769cbf51e14cd")).delete();
		System.out.println(helper.or().eq("_id", new ObjectId("58e6ebfd1a4769cbf51e2132")).eq("_id",
				new ObjectId("58e6eb3a1a4769cbf51e1f7c")).select());
		System.out.println(helper.or().eq("_id", new ObjectId("58e6ebfd1a4769cbf51e2132")).eq("_id",
				new ObjectId("58e6ed061a4769cbf51e2344")).delete());
		
		System.out.println(group.WebGroupPage(1, 2));
		System.out.println(group.WebGroupSelect());
	}
}
