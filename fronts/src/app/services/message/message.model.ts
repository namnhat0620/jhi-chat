export interface Message {
    id?: string;
    content?: string;
    sentToLogin?: string;
    sentFromLogin?: string;
    mediaUrl?: string;
    timestamp?: Date;
    messageType?: MessageType;
    groupId?: number;
}

export enum MessageType {
    TEXT = 'TEXT',
    IMAGE = 'IMAGE',
    VIDEO = 'VIDEO',
    FILE = 'FILE',
}