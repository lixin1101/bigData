package lx.web;

import java.util.Scanner;

import lx.demain.Product;
import lx.service.ProductService;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class ProductWeb {

	public static void main(String[] args) {
	/*	//创建键盘录入对象
		Scanner sc=new Scanner(System.in);
		//使用循环
		while(true) {
			//提示
			System.out.println("--------欢迎来到商品管理系统---------");
			System.out.println("输入以下命令操作：");
			System.out.println
			("C：添加商品 D：根据编号删除商品 DA：删除所有商品 I：根据商品编号查询商品信息 FA：查询所有商品信息 Q：退出");
			//获取输入的信息
			String inputChoice = sc.nextLine();
			//使用多分枝进行选择
			switch(inputChoice.toLowerCase()) {
			case "C":
				System.out.println("添加商品");
				addProduct();
				break;
			case "D":
				System.out.println("根据编号删除商品 ");
				deleteProductByPid();
				break;
			case "DA":
				System.out.println("删除所有商品 ");
				break;
			case "I":
				System.out.println("根据商品编号查询商品信息");
				findProductsByPid();
				break;
			case "FA":
				System.out.println("查询所有商品信息");
				//调用方法查询所有商品信息
				findAllProducts();
				break;
			*//*case "Q":
				System.out.println("退出");
				break;*//*
			default:
				//System.out.println("退出");
				System.out.println("谢谢光临");
				System.exit(0);
				break;
				
			}
			
		}*/
		
	}

	/**
	 * 根据编号删除商品
	 */
	/*private static void deleteProductByPid() {
		Scanner sc=new Scanner(System.in);
		System.out.println("请输入要删除商品编号:");
		String pidStr = sc.nextLine();
		int pid = Integer.parseInt(pidStr);
		//先查询数据库有没有数据
		ProductService ps = new ProductService();
		DBCursor cur = ps.findProductsByPid(pid);
		if(cur.size()==0) {
			System.out.println("对不起，没有删除的数据");
			return;
		}
		System.out.println("商品编号\t商品名称\t商品价格");
		while(cur.hasNext()) {
			//获取商品，获取每一行
			DBObject product = cur.next();
			//输出
			System.out.println
			(product.get("pid")+"\t"+product.get("pname")+"\t"+product.get("price"));
		}
		//提示是否要删除
		System.out.println("确定要删除么？按y");
		String yes = sc.nextLine();
		if("y".equals(yes)) {
			ps.deleteProductByPid(pid);
			System.out.println("删除成功");
		}else {
			System.out.println("取消删除");
		}
	}*/

	/**
	 * 添加商品
	 */
	/*private static void addProduct() {
		Scanner sc=new Scanner(System.in);
		System.out.println("请输入商品编号:");
		String pidStr = sc.nextLine();
		int pid = Integer.parseInt(pidStr);
		System.out.println("请输入商品名称:");
		String pname = sc.nextLine();
		System.out.println("请输入商品价格:");
		String p_price = sc.nextLine();
		int price = Integer.parseInt(p_price);
		
		Product p = new Product();
		
		p.setPid(pid);
		p.setPname(pname);
		p.setPrice(price);
		
		ProductService ps = new ProductService();
		ps.addProduct(p);
		System.out.println("商品添加成功");
	}*/

	/**
	 * 根据商品编号查询商品信息
	 */
	/*private static void findProductsByPid() {
		Scanner sc=new Scanner(System.in);
		System.out.println("请输入要查询的商品编号");
		String pidStr = sc.nextLine();
		int pid = Integer.parseInt(pidStr);
		ProductService ps=new ProductService();
		DBCursor cursor=ps.findProductsByPid(pid);
		if(cursor.size()!=0) {
			System.out.println("商品编号\t商品名称\t商品价格");
			while(cursor.hasNext()) {
				//获取商品，获取每一行
				DBObject product = cursor.next();
				//输出
				System.out.println(product.get("pid")+"\t"+product.get("pname")+"\t"+product.get("price"));
			
			}
		}else {
			System.out.println("没有商品");
		}
	}*/

	/**
	 * 调用方法查询所有商品信息
	 */
	/*private static void findAllProducts() {
		ProductService ps=new ProductService();
		DBCursor cursor=ps.findAllProducts();
		//根据cursor判断数据库中是否有数据
		if(cursor.size()==0) {
			System.out.println("没有数据");
		}else {
			//获取游标数据
			System.out.println("商品编号\t商品名称\t商品价格");
			while(cursor.hasNext()) {
				//获取商品，获取每一行
				DBObject product = cursor.next();
				//输出
				System.out.println(product.get("pid")+"\t"+product.get("pname")+"\t"+product.get("price"));
			}
		}
	}*/
}
