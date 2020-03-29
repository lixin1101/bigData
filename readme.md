#大数据项目模块
#聚合
GET gmall/PmsSkuInfo/_search
{
  "from" : 0,
  "size" : 20,
  "query" : {
    "bool" : {
      "must" : {
        "match" : {
          "skuName" : {
            "query" : "小米"
          }
        }
      }
    }
  }
  ,
  "aggs": {
    "name": {
      "terms": {
        "field": "skuAttrValueList.valueId"
     }
    }
  }
}


