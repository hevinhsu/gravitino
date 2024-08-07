/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.datastrato.gravitino.storage.relational.mapper;

import com.datastrato.gravitino.storage.relational.po.TablePO;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * A MyBatis Mapper for table meta operation SQLs.
 *
 * <p>This interface class is a specification defined by MyBatis. It requires this interface class
 * to identify the corresponding SQLs for execution. We can write SQLs in an additional XML file, or
 * write SQLs with annotations in this interface Mapper. See: <a
 * href="https://mybatis.org/mybatis-3/getting-started.html"></a>
 */
public interface TableMetaMapper {
  String TABLE_NAME = "table_meta";

  @Select(
      "SELECT table_id as tableId, table_name as tableName,"
          + " metalake_id as metalakeId, catalog_id as catalogId,"
          + " schema_id as schemaId, audit_info as auditInfo,"
          + " current_version as currentVersion, last_version as lastVersion,"
          + " deleted_at as deletedAt"
          + " FROM "
          + TABLE_NAME
          + " WHERE schema_id = #{schemaId} AND deleted_at = 0")
  List<TablePO> listTablePOsBySchemaId(@Param("schemaId") Long schemaId);

  @Select(
      "SELECT table_id as tableId FROM "
          + TABLE_NAME
          + " WHERE schema_id = #{schemaId} AND table_name = #{tableName}"
          + " AND deleted_at = 0")
  Long selectTableIdBySchemaIdAndName(
      @Param("schemaId") Long schemaId, @Param("tableName") String name);

  @Select(
      "SELECT table_id as tableId, table_name as tableName,"
          + " metalake_id as metalakeId, catalog_id as catalogId,"
          + " schema_id as schemaId, audit_info as auditInfo,"
          + " current_version as currentVersion, last_version as lastVersion,"
          + " deleted_at as deletedAt"
          + " FROM "
          + TABLE_NAME
          + " WHERE schema_id = #{schemaId} AND table_name = #{tableName} AND deleted_at = 0")
  TablePO selectTableMetaBySchemaIdAndName(
      @Param("schemaId") Long schemaId, @Param("tableName") String name);

  @Select(
      "SELECT table_id as tableId, table_name as tableName,"
          + " metalake_id as metalakeId, catalog_id as catalogId,"
          + " schema_id as schemaId, audit_info as auditInfo,"
          + " current_version as currentVersion, last_version as lastVersion,"
          + " deleted_at as deletedAt"
          + " FROM "
          + TABLE_NAME
          + " WHERE table_id = #{tableId} AND deleted_at = 0")
  TablePO selectTableMetaById(@Param("tableId") Long tableId);

  @Insert(
      "INSERT INTO "
          + TABLE_NAME
          + "(table_id, table_name, metalake_id,"
          + " catalog_id, schema_id, audit_info,"
          + " current_version, last_version, deleted_at)"
          + " VALUES("
          + " #{tableMeta.tableId},"
          + " #{tableMeta.tableName},"
          + " #{tableMeta.metalakeId},"
          + " #{tableMeta.catalogId},"
          + " #{tableMeta.schemaId},"
          + " #{tableMeta.auditInfo},"
          + " #{tableMeta.currentVersion},"
          + " #{tableMeta.lastVersion},"
          + " #{tableMeta.deletedAt}"
          + " )")
  void insertTableMeta(@Param("tableMeta") TablePO tablePO);

  @Insert(
      "INSERT INTO "
          + TABLE_NAME
          + "(table_id, table_name, metalake_id,"
          + " catalog_id, schema_id, audit_info,"
          + " current_version, last_version, deleted_at)"
          + " VALUES("
          + " #{tableMeta.tableId},"
          + " #{tableMeta.tableName},"
          + " #{tableMeta.metalakeId},"
          + " #{tableMeta.catalogId},"
          + " #{tableMeta.schemaId},"
          + " #{tableMeta.auditInfo},"
          + " #{tableMeta.currentVersion},"
          + " #{tableMeta.lastVersion},"
          + " #{tableMeta.deletedAt}"
          + " )"
          + " ON DUPLICATE KEY UPDATE"
          + " table_name = #{tableMeta.tableName},"
          + " metalake_id = #{tableMeta.metalakeId},"
          + " catalog_id = #{tableMeta.catalogId},"
          + " schema_id = #{tableMeta.schemaId},"
          + " audit_info = #{tableMeta.auditInfo},"
          + " current_version = #{tableMeta.currentVersion},"
          + " last_version = #{tableMeta.lastVersion},"
          + " deleted_at = #{tableMeta.deletedAt}")
  void insertTableMetaOnDuplicateKeyUpdate(@Param("tableMeta") TablePO tablePO);

  @Update(
      "UPDATE "
          + TABLE_NAME
          + " SET table_name = #{newTableMeta.tableName},"
          + " metalake_id = #{newTableMeta.metalakeId},"
          + " catalog_id = #{newTableMeta.catalogId},"
          + " schema_id = #{newTableMeta.schemaId},"
          + " audit_info = #{newTableMeta.auditInfo},"
          + " current_version = #{newTableMeta.currentVersion},"
          + " last_version = #{newTableMeta.lastVersion},"
          + " deleted_at = #{newTableMeta.deletedAt}"
          + " WHERE table_id = #{oldTableMeta.tableId}"
          + " AND table_name = #{oldTableMeta.tableName}"
          + " AND metalake_id = #{oldTableMeta.metalakeId}"
          + " AND catalog_id = #{oldTableMeta.catalogId}"
          + " AND schema_id = #{oldTableMeta.schemaId}"
          + " AND audit_info = #{oldTableMeta.auditInfo}"
          + " AND current_version = #{oldTableMeta.currentVersion}"
          + " AND last_version = #{oldTableMeta.lastVersion}"
          + " AND deleted_at = 0")
  Integer updateTableMeta(
      @Param("newTableMeta") TablePO newTablePO, @Param("oldTableMeta") TablePO oldTablePO);

  @Update(
      "UPDATE "
          + TABLE_NAME
          + " SET deleted_at = (UNIX_TIMESTAMP() * 1000.0)"
          + " + EXTRACT(MICROSECOND FROM CURRENT_TIMESTAMP(3)) / 1000"
          + " WHERE table_id = #{tableId} AND deleted_at = 0")
  Integer softDeleteTableMetasByTableId(@Param("tableId") Long tableId);

  @Update(
      "UPDATE "
          + TABLE_NAME
          + " SET deleted_at = (UNIX_TIMESTAMP() * 1000.0)"
          + " + EXTRACT(MICROSECOND FROM CURRENT_TIMESTAMP(3)) / 1000"
          + " WHERE metalake_id = #{metalakeId} AND deleted_at = 0")
  Integer softDeleteTableMetasByMetalakeId(@Param("metalakeId") Long metalakeId);

  @Update(
      "UPDATE "
          + TABLE_NAME
          + " SET deleted_at = (UNIX_TIMESTAMP() * 1000.0)"
          + " + EXTRACT(MICROSECOND FROM CURRENT_TIMESTAMP(3)) / 1000"
          + " WHERE catalog_id = #{catalogId} AND deleted_at = 0")
  Integer softDeleteTableMetasByCatalogId(@Param("catalogId") Long catalogId);

  @Update(
      "UPDATE "
          + TABLE_NAME
          + " SET deleted_at = (UNIX_TIMESTAMP() * 1000.0)"
          + " + EXTRACT(MICROSECOND FROM CURRENT_TIMESTAMP(3)) / 1000"
          + " WHERE schema_id = #{schemaId} AND deleted_at = 0")
  Integer softDeleteTableMetasBySchemaId(@Param("schemaId") Long schemaId);

  @Delete(
      "DELETE FROM "
          + TABLE_NAME
          + " WHERE deleted_at > 0 AND deleted_at < #{legacyTimeline} LIMIT #{limit}")
  Integer deleteTableMetasByLegacyTimeline(
      @Param("legacyTimeline") Long legacyTimeline, @Param("limit") int limit);
}
