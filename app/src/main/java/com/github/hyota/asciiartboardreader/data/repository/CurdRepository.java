package com.github.hyota.asciiartboardreader.data.repository;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * CURD操作提供リポジトリ.
 *
 * @param <T>  操作対象の型
 * @param <ID> プライマリキー
 */
public interface CurdRepository<T, ID extends Serializable> {

    /**
     * 指定したIDに対応するEntityを取得する.
     *
     * @param id ID
     * @return Entity
     */
    @Nullable
    T findOne(ID id);

    /**
     * 指定したIDに対応するEntityが存在するか判定する.
     *
     * @param id ID
     * @return 判定結果
     */
    boolean exists(ID id);

    /**
     * 全てのEntityを取得する.
     *
     * @return すべてのEntity
     */
    @Nonnull
    List<T> findAll();

    /**
     * Entityの総件数を取得する.
     *
     * @return Entityの総数
     */
    long count();

    /**
     * Entityを保存する.
     *
     * @param entity Entity
     * @return Entity
     */
    @Nullable
    T save(@Nonnull T entity);

    /**
     * Entityを削除する.
     *
     * @param entity Entity
     */
    void delete(@Nonnull T entity);

}
