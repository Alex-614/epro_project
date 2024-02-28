export interface Businessunitkeyresult {
    buId: number,
    buoId: number,
    id: number,
    title: string,
    type: string,
    goal: number,
    description: string,
    current: number,
    confidenceLevel: number,
    achievement: number,
    contributingUnits: { id: number, businessUnitId: {id: number, companyId: number} }[],
    contributingBusinessUnits: []
}
