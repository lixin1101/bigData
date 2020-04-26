package lx.hbase.crud;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

public class TestCRUD {
    @Test
    public void put() throws Exception {
        //创建conf对象
        Configuration conf = HBaseConfiguration.create();
        //通过连接工厂创建连接对象
        Connection conn = ConnectionFactory.createConnection(conf);
        //通过连接查询tableName对象
        TableName tname = TableName.valueOf("ns1:t1");
        //获得table
        Table table = conn.getTable(tname);

        //通过bytes工具类创建字节数组(将字符串)
        byte[] rowid = Bytes.toBytes("row3");

        //创建put对象
        Put put = new Put(rowid);

        byte[] f1 = Bytes.toBytes("f1");
        byte[] id = Bytes.toBytes("id");
        byte[] value = Bytes.toBytes(102);
        put.addColumn(f1, id, value);

        //执行插入
        table.put(put);
    }

    @Test
    public void bigInsert() throws Exception {
        DecimalFormat format = new DecimalFormat();
        format.applyPattern("0000");

        long start = System.currentTimeMillis();
        //创建conf对象
        Configuration conf = HBaseConfiguration.create();
        //通过连接工厂创建连接对象
        Connection conn = ConnectionFactory.createConnection(conf);
        //通过连接查询tableName对象
        TableName tname = TableName.valueOf("ns1:t1");
        //获得table
        HTable table = (HTable) conn.getTable(tname);
        //不要自动清理缓冲区
        table.setAutoFlush(false);
        for (int i = 1; i < 10000; i++) {
            Put put = new Put(Bytes.toBytes("row" + format.format(i)));
            //关闭写前日志
            put.setWriteToWAL(false);
            put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("id"), Bytes.toBytes(i));
            put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("name"), Bytes.toBytes("tom" + i));
            put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("age"), Bytes.toBytes(i % 100));
            table.put(put);

            if (i % 2000 == 0) {
                table.flushCommits();
            }
        }
        table.flushCommits();
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void formatNum() {
        DecimalFormat format = new DecimalFormat();
        format.applyPattern("0000000");
        System.out.println(format.format(8));//0000008

    }

    @Test
    public void get() throws Exception {
        //创建conf对象
        Configuration conf = HBaseConfiguration.create();
        //通过连接工厂创建连接对象
        Connection conn = ConnectionFactory.createConnection(conf);
        //通过连接查询tableName对象
        TableName tname = TableName.valueOf("ns1:t1");
        //获得table
        Table table = conn.getTable(tname);

        if (Bytes.toBytes("row3").equals("")) {
            System.out.println("没有这个rowkey");
        } else {
            //通过bytes工具类创建字节数组(将字符串)
            byte[] rowid = Bytes.toBytes("row3");
            Get get = new Get(Bytes.toBytes("row3"));
            Result r = table.get(get);
            byte[] idvalue = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
            System.out.println(Bytes.toInt(idvalue));
        }
    }

    @Test
    public void createNameSpace() throws Exception {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        //创建名字空间描述符
        NamespaceDescriptor nsd = NamespaceDescriptor.create("ns2").build();
        admin.createNamespace(nsd);

        NamespaceDescriptor[] ns = admin.listNamespaceDescriptors();
        for(NamespaceDescriptor n : ns){
            System.out.println(n.getName());
        }
    }

    @Test
    public void listNameSpaces() throws Exception {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();

        NamespaceDescriptor[] ns = admin.listNamespaceDescriptors();
        for(NamespaceDescriptor n : ns){
            System.out.println(n.getName());
        }
    }

    @Test
    public void createTable() throws Exception {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        //创建表名对象
        TableName tableName = TableName.valueOf("ns2:t2");
        //创建表描述符对象
        HTableDescriptor tbl = new HTableDescriptor(tableName);
        //创建列族描述符
        HColumnDescriptor col = new HColumnDescriptor("f1");
        tbl.addFamily(col);

        admin.createTable(tbl);
        System.out.println("over");
    }

    /*@Test
    public void disableTable() throws Exception {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        Admin admin = conn.getAdmin();
        //禁用表 enable(...) disableTable(...)  禁用表后不能put数据和scan 或者get 数据
        admin.deleteTable(TableName.valueOf("ns2:t2"));
    }
*/
    /**
     * 删除数据
     */
    @Test
    public void deleteData() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t1");

        Table table = conn.getTable(tname);
        Delete del = new Delete(Bytes.toBytes("row0003959"));
        del.addColumn(Bytes.toBytes("f1"),Bytes.toBytes("id"));
        del.addColumn(Bytes.toBytes("f1"),Bytes.toBytes("name"));
        table.delete(del);
        System.out.println("over");
    }


    @Test
    public void scan() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t1");
        Table table = conn.getTable(tname);
        Scan scan = new Scan();
        //切记不要全表扫描
        //前闭后开
        scan.setStartRow(Bytes.toBytes("row0003959"));
        scan.setStopRow(Bytes.toBytes("row0003972"));
        ResultScanner rs = table.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()) {
            Result r = it.next();
            byte[] name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
            System.out.println(Bytes.toString(name));
        }
    }

    /**
     * 动态遍历
     */
    @Test
    public void scan2() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t1");
        Table table = conn.getTable(tname);
        Scan scan = new Scan();
        scan.setStartRow(Bytes.toBytes("row0003959"));
        scan.setStopRow(Bytes.toBytes("row0003972"));
        ResultScanner rs = table.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()) {
            Result r = it.next();
            Map<byte[],byte[]> map = r.getFamilyMap(Bytes.toBytes("f1"));
            for(Map.Entry<byte[],byte[]> entrySet : map.entrySet()){
                String col = Bytes.toString(entrySet.getKey());
                String val = Bytes.toString(entrySet.getValue());
                System.out.print(col + ":" + val + ",");
            }

            System.out.println();
        }
    }

    /**
     * 动态遍历
     */
    @Test
    public void scan3() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t1");
        Table table = conn.getTable(tname);
        Scan scan = new Scan();
        scan.setStartRow(Bytes.toBytes("row5000"));
        scan.setStopRow(Bytes.toBytes("row8000"));
        ResultScanner rs = table.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()) {
            Result r = it.next();
            //得到一行的所有map,key=f1,value=Map<Col,Map<Timestamp,value>>
            NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> map = r.getMap();
            for(Map.Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> entry : map.entrySet()){
                //得到列族
                String f = Bytes.toString(entry.getKey());
                Map<byte[], NavigableMap<Long, byte[]>> colDataMap = entry.getValue();
                for(Map.Entry<byte[], NavigableMap<Long, byte[]>> ets : colDataMap.entrySet() ){
                    String c = Bytes.toString(ets.getKey());
                    Map<Long, byte[]> tsValueMap = ets.getValue();
                    for(Map.Entry<Long,byte[]> e : tsValueMap.entrySet()){
                        Long ts = e.getKey() ;
                        String value = Bytes.toString(e.getValue());
                        System.out.print(f+":"+c+":"+ts+"=" +value + ",");
                    }
                }
            }
            System.out.println();
        }
    }

    /**
     * 按照指定版本数查询
     */
    @Test
    public void getWithVersions() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t3");
        Table table = conn.getTable(tname);
        Get get = new Get(Bytes.toBytes("row1"));
        //检索所有版本,可以无参，可以设置参数
        get.setMaxVersions();
        //可以设置时间戳范围
        //get.setTimeRange(1554438793463L,1554438810866L);
        Result r = table.get(get);
        List<Cell> cells = r.getColumnCells(Bytes.toBytes("f1"), Bytes.toBytes("name"));
        for(Cell c : cells){
            String f = Bytes.toString(c.getFamily());
            String col = Bytes.toString(c.getQualifier());
            long ts = c.getTimestamp();
            String val = Bytes.toString(c.getValue());
            System.out.println(f + "/" + col + "/" + ts + "=" + val);
        }
    }

    @Test
    public void getScanCache() throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t1");
        Scan scan = new Scan();
        //为什么要设置缓存？降低扫描次数，先保存到内存，然后再取出数据，速度变快
        scan.setCaching(5000);
        Table t = conn.getTable(tname);
        ResultScanner rs = t.getScanner(scan);
        long start = System.currentTimeMillis();
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()) {
            Result r = it.next();
            System.out.println(r.getColumnLatestCell(Bytes.toBytes("f1"), Bytes.toBytes("name")));
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    /**
     * 测试缓存和批处理
     *
     * ========================================
     * f1/id/1490595148588=1,f2/addr/1490595182150=hebei,f2/age/1490595174760=12,
     * ========================================
     * f2/id/1490595164473=1,f2/name/1490595169589=tom,
     *
     * ========================================
     * f1/id/1490595196410=2,f1/name/1490595213090=tom2.1,f2/addr/1490595264734=tangshan,
     * ========================================
     * f2/age/1490595253996=13,f2/id/1490595233568=2,f2/name/1490595241891=tom2.2,
     *
     * ========================================
     * f1/age/1490595295427=14,f1/id/1490595281251=3,f1/name/1490595289587=tom3.1,
     * ========================================
     * f2/addr/1490595343690=beijing,f2/age/1490595336300=14,f2/id/1490595310966=3,
     *
     * ========================================
     * f2/name/1490595327531=tom3.2,
     */
    @Test
    public void testBatchAndCaching() throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t7");
        Scan scan = new Scan();
        //缓存和批处理，加快速度
        scan.setCaching(2);
        scan.setBatch(4);//控制每次next()服务器端返回的列的个数。 每次next返回4列。
        Table t = conn.getTable(tname);
        ResultScanner rs = t.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()) {
            Result r = it.next();
            System.out.println("========================================");
            //得到一行的所有map,key=f1,value=Map<Col,Map<Timestamp,value>>
            NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> map = r.getMap();
            for (Map.Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> entry : map.entrySet()) {
                //得到列族
                String f = Bytes.toString(entry.getKey());
                Map<byte[], NavigableMap<Long, byte[]>> colDataMap = entry.getValue();
                for (Map.Entry<byte[], NavigableMap<Long, byte[]>> ets : colDataMap.entrySet()) {
                    String c = Bytes.toString(ets.getKey());
                    Map<Long, byte[]> tsValueMap = ets.getValue();
                    for (Map.Entry<Long, byte[]> e : tsValueMap.entrySet()) {
                        Long ts = e.getKey();
                        String value = Bytes.toString(e.getValue());
                        System.out.print(f + "/" + c + "/" + ts + "=" + value + ",");
                    }
                }
            }
            System.out.println();
        }
    }

    /**
     * 测试RowFilter过滤器
     */
    @Test
    public void testRowFilter() throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t1");
        Scan scan = new Scan();
        RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.LESS_OR_EQUAL, new BinaryComparator(Bytes.toBytes("row0100")));
        //RegexStringComparator
        //RowFilter rowFilter2 = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator("^\\d{3}$"));
        scan.setFilter(rowFilter);
        Table t = conn.getTable(tname);
        ResultScanner rs = t.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()) {
            Result r = it.next();
            System.out.println(Bytes.toString(r.getRow()));
        }
    }

    /**
     * 测试RowFilter过滤器
     */
    @Test
    public void testRowFilter2() throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t1");
        Scan scan = new Scan();
        RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("row0100")));
        //RegexStringComparator
        RowFilter rowFilter2 = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator("^\\d{3}$"));
        //FilterList.Operator.MUST_PASS_ALL  == > and
        //FilterList.Operator.MUST_PASS_ONE  == > or
        FilterList list=new FilterList(FilterList.Operator.MUST_PASS_ONE);

        list.addFilter(rowFilter);
        list.addFilter(rowFilter2);

        scan.setFilter(list);

        Table t = conn.getTable(tname);
        ResultScanner rs = t.getScanner(scan);
        Iterator<Result> it = rs.iterator();

        while (it.hasNext()) {
            Result r = it.next();
            System.out.println(Bytes.toString(r.getRow()));
        }
    }

    /**
     * 测试FamilyFilter过滤器
     */
    @Test
    public void testFamilyFilter() throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t7");
        Scan scan = new Scan();
        FamilyFilter filter = new FamilyFilter(CompareFilter.CompareOp.LESS, new BinaryComparator(Bytes.toBytes("f2")));
        scan.setFilter(filter);
        Table t = conn.getTable(tname);
        ResultScanner rs = t.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()) {
            Result r = it.next();
            byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
            byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
            System.out.println(f1id + " : " + f2id);
        }
    }

    /**
     * 测试QualifierFilter(列过滤器)
     */
    @Test
    public void testColFilter() throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t7");
        Scan scan = new Scan();
        QualifierFilter colfilter = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("id")));
        scan.setFilter(colfilter);
        Table t = conn.getTable(tname);
        ResultScanner rs = t.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()) {
            Result r = it.next();
            byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
            byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
            byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
            System.out.println(f1id + " : " + f2id + " : " + f2name);
        }
    }


    /**
     * 测试ValueFilter(值过滤器)
     * 过滤value的值，含有指定的字符子串
     */
    @Test
    public void testValueFilter() throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t7");
        Scan scan = new Scan();
        ValueFilter filter = new ValueFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("to"));
        scan.setFilter(filter);
        Table t = conn.getTable(tname);
        ResultScanner rs = t.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()) {
            Result r = it.next();
            byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
            byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
            byte[] f1name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
            byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
            System.out.println(f1id + " : " + f2id + " : " + Bytes.toString(f1name) + " : " + Bytes.toString(f2name));
        }
    }

    /**
     * 依赖列过滤器
     */
    @Test
    public void testDepFilter() throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t7");
        Scan scan = new Scan();
        DependentColumnFilter filter = new DependentColumnFilter(Bytes.toBytes("f2"),
                Bytes.toBytes("name"),
                false,
                CompareFilter.CompareOp.EQUAL,
                new BinaryComparator(Bytes.toBytes("tom2.2"))
        );

        //ValueFilter filter = new ValueFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("to"));
        scan.setFilter(filter);
        Table t = conn.getTable(tname);
        ResultScanner rs = t.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()) {
            Result r = it.next();
            byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
            byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
            byte[] f1name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
            byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
            System.out.println(f1id + " : " + f2id + " : " + Bytes.toString(f1name) + " : " + Bytes.toString(f2name));
        }
    }

    /**
     * 单列值value过滤，对列上的value进行过滤，不符合整行删除。
     */
    @Test
    public void testSingleColumValueFilter() throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t7");
        Scan scan = new Scan();
        SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes("f2"),
                Bytes.toBytes("name"),
                CompareFilter.CompareOp.NOT_EQUAL,
                new BinaryComparator(Bytes.toBytes("tom2.2")));

        scan.setFilter(filter);
        Table t = conn.getTable(tname);
        ResultScanner rs = t.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()) {
            Result r = it.next();
            byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
            byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
            byte[] f1name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
            byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
            System.out.println(f1id + " : " + f2id + " : " + Bytes.toString(f1name) + " : " + Bytes.toString(f2name));
        }
    }

    /**
     * 单列值排除过滤器,去掉过滤使用的列,对列的值进行过滤
     */
    @Test
    public void testSingleColumValueExcludeFilter() throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t7");
        Scan scan = new Scan();
        SingleColumnValueExcludeFilter filter = new SingleColumnValueExcludeFilter(Bytes.toBytes("f2"),
                Bytes.toBytes("name"),
                CompareFilter.CompareOp.EQUAL,
                new BinaryComparator(Bytes.toBytes("tom2.2")));

        scan.setFilter(filter);
        Table t = conn.getTable(tname);
        ResultScanner rs = t.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()) {
            Result r = it.next();
            byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
            byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
            byte[] f1name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
            byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
            System.out.println(f1id + " : " + f2id + " : " + Bytes.toString(f1name) + " : " + Bytes.toString(f2name));
        }
    }


    /**
     * 前缀过滤,是rowkey过滤. where rowkey like 'row22%'
     */
    @Test
    public void testPrefixFilter() throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t1");
        Scan scan = new Scan();
        PrefixFilter filter = new PrefixFilter(Bytes.toBytes("row222"));

        scan.setFilter(filter);
        Table t = conn.getTable(tname);
        ResultScanner rs = t.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()) {
            Result r = it.next();
            byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
            byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
            byte[] f1name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
            byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
            System.out.println(f1id + " : " + f2id + " : " + Bytes.toString(f1name) + " : " + Bytes.toString(f2name));
        }
    }


    /**
     * 分页过滤,是rowkey过滤,在region上扫描时，对每次page设置的大小。
     * 返回到到client，设计到每个Region结果的合并。
     */
    @Test
    public void testPageFilter() throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t1");
        Scan scan = new Scan();

        PageFilter filter = new PageFilter(10);

        scan.setFilter(filter);
        Table t = conn.getTable(tname);
        ResultScanner rs = t.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()) {
            Result r = it.next();
            byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
            byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
            byte[] f1name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
            byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
            System.out.println(f1id + " : " + f2id + " : " + Bytes.toString(f1name) + " : " + Bytes.toString(f2name));
        }
    }

    /**
     * keyOnly过滤器，只提取key,丢弃value.
     * @throws IOException
     */
    @Test
    public void testKeyOnlyFilter() throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t1");
        Scan scan = new Scan();

        KeyOnlyFilter filter = new KeyOnlyFilter();

        scan.setFilter(filter);
        Table t = conn.getTable(tname);
        ResultScanner rs = t.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()) {
            Result r = it.next();
            byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
            byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
            byte[] f1name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
            byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
            System.out.println(f1id + " : " + f2id + " : " + Bytes.toString(f1name) + " : " + Bytes.toString(f2name));
        }
    }


    /**
     * ColumnPageFilter,列分页过滤器，过滤指定范围列，
     * select ,,a,b from ns1:t7
     */
    @Test
    public void testColumnPageFilter() throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t7");
        Scan scan = new Scan();

        ColumnPaginationFilter filter = new ColumnPaginationFilter(2,2);

        scan.setFilter(filter);
        Table t = conn.getTable(tname);
        ResultScanner rs = t.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()) {
            Result r = it.next();
            byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
            byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
            byte[] f1name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
            byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
            System.out.println(f1id + " : " + f2id + " : " + Bytes.toString(f1name) + " : " + Bytes.toString(f2name));
        }
    }


    /**
     *
     */
    @Test
    public void testLike() throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t7");
        Scan scan = new Scan();

        ValueFilter filter = new ValueFilter(CompareFilter.CompareOp.EQUAL,
                new RegexStringComparator("^tom2")
        );

        scan.setFilter(filter);
        Table t = conn.getTable(tname);
        ResultScanner rs = t.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()) {
            Result r = it.next();
            byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
            byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
            byte[] f1name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
            byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
            System.out.println(f1id + " : " + f2id + " : " + Bytes.toString(f1name) + " : " + Bytes.toString(f2name));
        }
    }

    @Test
    public void testComboFilter() throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t7");
        Scan scan = new Scan();

        //where ... f2:age <= 13
        SingleColumnValueFilter ftl = new SingleColumnValueFilter(
                Bytes.toBytes("f2"),
                Bytes.toBytes("age"),
                CompareFilter.CompareOp.LESS_OR_EQUAL,
                new BinaryComparator(Bytes.toBytes("13"))
        );

        //where ... f2:name like %t
        SingleColumnValueFilter ftr = new SingleColumnValueFilter(
                Bytes.toBytes("f2"),
                Bytes.toBytes("name"),
                CompareFilter.CompareOp.EQUAL,
                new RegexStringComparator("^t")
        );
        //ft
        FilterList ft = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        ft.addFilter(ftl);
        ft.addFilter(ftr);

        //where ... f2:age > 13
        SingleColumnValueFilter fbl = new SingleColumnValueFilter(
                Bytes.toBytes("f2"),
                Bytes.toBytes("age"),
                CompareFilter.CompareOp.GREATER,
                new BinaryComparator(Bytes.toBytes("13"))
        );

        //where ... f2:name like %t
        SingleColumnValueFilter fbr = new SingleColumnValueFilter(
                Bytes.toBytes("f2"),
                Bytes.toBytes("name"),
                CompareFilter.CompareOp.EQUAL,
                new RegexStringComparator("t$")
        );
        //ft
        FilterList fb = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        fb.addFilter(fbl);
        fb.addFilter(fbr);


        FilterList fall = new FilterList(FilterList.Operator.MUST_PASS_ONE);
        fall.addFilter(ft);
        fall.addFilter(fb);

        scan.setFilter(fall);
        Table t = conn.getTable(tname);
        ResultScanner rs = t.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()) {
            Result r = it.next();
            byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
            byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
            byte[] f1name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
            byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
            System.out.println(f1id + " : " + f2id + " : " + Bytes.toString(f1name) + " : " + Bytes.toString(f2name));
        }
    }

    /**
     *测试计数器
     */
    @Test
    public void testIncr() throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t8");
        Table t = conn.getTable(tname);
        Increment incr = new Increment(Bytes.toBytes("row1"));
        incr.addColumn(Bytes.toBytes("f1"),Bytes.toBytes("daily"),1);
        incr.addColumn(Bytes.toBytes("f1"),Bytes.toBytes("weekly"),10);
        incr.addColumn(Bytes.toBytes("f1"),Bytes.toBytes("monthly"),100);
        t.increment(incr);

    }

}
