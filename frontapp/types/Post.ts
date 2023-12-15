import Comment from "@/types/Comment";
import Member from "@/types/Member";

export default interface Post {
    id: number;
    title: string;
    body: string;
    published: boolean;
    createDate: Date;
    modifyDate: Date;
    member: Member;
    hit: number;
    comments: Comment[];
}