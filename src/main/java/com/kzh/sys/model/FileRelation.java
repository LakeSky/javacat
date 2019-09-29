package com.kzh.sys.model;

import com.kzh.sys.enums.FileType;
import com.kzh.sys.service.generate.auto.QClass;
import com.kzh.sys.service.generate.auto.QField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by gang on 2017/3/22.
 */
@QClass(name = "实体与上传文件关联表")
@Entity
@Table(name = "sys_file_relation")
@Data
@EqualsAndHashCode(callSuper = false)
public class FileRelation extends BaseEntity {
    @QField(name = "文件类型")
    @Column(name = "file_type")
    @Enumerated(EnumType.STRING)
    private FileType fileType;

    @QField(name = "实体id")
    @Column(name = "obj_id")
    private String objId;//实体类物理id

    @QField(name = "文件id")
    @Column(name = "file_id")
    private String fileId;//上传文件的物理id
}
