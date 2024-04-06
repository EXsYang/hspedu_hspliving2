import http from '@/utils/httpRequest.js'
export function policy() {
   return  new Promise((resolve,reject)=>{
        http({
            //走网关的url
            //完整的走网关的url是: http://localhost:5050/api/service/oss/policy
            url: http.adornUrl("/service/oss/policy"),
            //这里老师写成固定的，后面通过nacos和网关解决
            //下面这个url是没有走网关的url
            // url: "http://localhost:7070/oss/policy",
            method: "get",
            params: http.adornParams({})
        }).then(({ data }) => {
            resolve(data);
        })
    });
}
