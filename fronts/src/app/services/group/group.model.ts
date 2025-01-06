export interface Group {
    id?: number,
    type?: string,
    lastMessageId?: string,
    avatar?: string,
    userGroups?: UserGroup[]
}

export interface UserGroup {
    id?: number,
    login?: string,
    isSeen?: boolean,
    isTurnOnNoti?: boolean
}

export enum GroupType {
    PRIVATE = 'PRIVATE',
    GROUP = 'GROUP'
}