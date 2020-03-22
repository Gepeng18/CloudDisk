create schema if not exists cloudDisk collate utf8mb4_0900_ai_ci;

create table if not exists ebook_contents
(
    id int auto_increment comment '条目ID'
        primary key,
    content_id varchar(100) comment '内容ID',
    content longtext null comment '内容',
    file_id int null comment '对应的文件的ID'
);

create table if not exists ebooks
(
    ebook_id int auto_increment comment '书ID'
        primary key,
    ebook_name varchar(100) null comment '书名',
    file_id int comment '对应的文件ID',
    header longtext null comment '书的目录内容'
);

create table if not exists file_folder
(
    file_folder_id int auto_increment comment '文件夹ID'
        primary key,
    file_folder_name varchar(255) null comment '文件夹名称',
    parent_folder_id int default 0 null comment '父文件夹ID',
    user_id int null comment '所属用户ID',
    time datetime null comment '创建时间'
)
    charset=utf8;



create table if not exists my_file
(
    my_file_id int auto_increment comment '文件ID'
        primary key,
    my_file_name varchar(255) null comment '文件名',
    show_path varchar(255) null comment '在线预览路径',
    user_id int null comment '用户ID',
    my_file_path varchar(255) default '/' null comment '文件存储路径',
    download_time int default 0 null comment '下载次数',
    upload_time datetime null comment '上传时间',
    parent_folder_id int null comment '父文件夹ID',
    size int null comment '文件大小',
    type int null comment '文件类型',
    postfix varchar(255) null comment '文件后缀'
)
    charset=utf8;

create table if not exists site_setting
(
    id int auto_increment
        primary key,
    setting int not null,
    description varchar(10) not null
);

create table if not exists user
(
    user_id int unsigned auto_increment comment '用户ID'
        primary key,
    open_id varchar(255) null comment '用户的openid',
    user_name varchar(50) null comment '用户名',
    register_time datetime null comment '注册时间',
    image_path varchar(255) default '' null comment '头像地址',
    current_size int default 0 null comment '当前容量（单位KB）',
    max_size int default 1048576 null comment '最大容量（单位KB）'
)
    charset=utf8;

