# QueryDsl 基本操作

	相关性代码 存在于 plus.ojbk.querydsl.QuerydslApplicationTests.java 中自行查阅
	
# 准备工作

    如果你下载下来的代码 把target目录删除了 
    
    那么你的test目录将会报红 但问题不大
    
    你需要打开idea的terminal 或者使用cmd 命令cd到文件根目录
    
    输入 mvn compile -DskipTests
    
    然后你打开 target目录下的generated-sources就会发现下面有 java这个目录
    
    取决于你 pom文件中定义的路径  <outputDirectory>target/generated-sources/java</outputDirectory>
    
    idea中 右键点击 这个generated-sources 底下的java目录
 
    出现选择选项  选择 Mark Directory as  -> Sources Root
    
    这样你的报红就基本解决了
 
    打开resource目录中的 application.properties
    
    增加mysql的相关性配置 填写你自己的
    