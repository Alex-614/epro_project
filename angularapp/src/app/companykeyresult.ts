import { Businessunit } from "./businessunit"

export interface Companykeyresult {
    id: string,
    title: string,
    type: string,
    goal: number,
    description: string,
    current: number,
    confidenceLevel: number,
    achievement: number,
    contributingBusinessUnits: { id: number, companyId: string}[]
}