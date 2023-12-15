'use client';
import { instance } from "@/config/axiosConfig";
import Post from "@/types/Post";
import RsData from "@/types/rsData";
import formatDate from "@/util/formatDate";
import {
    getKeyValue,
    Link,
    Pagination,
    Table,
    TableBody,
    TableCell,
    TableColumn,
    TableHeader,
    TableRow
} from "@nextui-org/react";
import React, { useEffect, useState } from "react";


export default function Home() {
    useEffect(() => {
        instance.get("/post/latest").then((res) => {
            const rsData: RsData = res.data;

            rsData.data.content = rsData.data.content.map((post: any) => {
                post.createDate = formatDate(new Date(post.createDate));
                return post;
            })

            setPosts(rsData.data.content);
        })
    }, []);

    const [posts, setPosts] = useState<Post[]>([]);

    const [page, setPage] = React.useState(1);
    const rowsPerPage = 10;

    const pages = Math.ceil(posts.length / rowsPerPage);

    const items = React.useMemo(() => {
        const start = (page - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        return posts.slice(start, end);
    }, [page, posts]);

    return (
        <div className={"flex flex-col items-center"}>
            <h1 className={"font-bold text-4xl"}>최신 글</h1>
            <Table
                aria-label="Example table with client side pagination"
                bottomContent={
                    <div className="flex w-full justify-center">
                        <Pagination
                            isCompact
                            showControls
                            showShadow
                            color="secondary"
                            page={page}
                            total={pages}
                            onChange={(page) => setPage(page)}
                        />
                    </div>
                }
                classNames={{
                    wrapper: "min-h-[222px] mt-5",
                }}
            >
                <TableHeader>
                    <TableColumn key="title">제목</TableColumn>
                    <TableColumn key="createDate">작성일</TableColumn>
                    <TableColumn key="username">작성자</TableColumn>
                    <TableColumn key="hit">조회수</TableColumn>
                </TableHeader>
                <TableBody items={items}>
                    {(item) => (
                        <TableRow key={item.id}>
                            {(columnKey) => {
                                if (columnKey === 'username') {
                                    return (
                                        <TableCell>{getKeyValue(item.member, columnKey)}</TableCell>
                                    )
                                } else if (columnKey === 'title') {
                                    return (
                                        <TableCell>
                                            <Link size={"sm"}
                                                href={`/post/${(item as Post).id}`}>{getKeyValue(item, columnKey)}</Link>
                                        </TableCell>
                                    )
                                }
                                return <TableCell>{getKeyValue(item, columnKey)}</TableCell>
                            }}
                        </TableRow>
                    )}
                </TableBody>
            </Table>
        </div>
    );
}
