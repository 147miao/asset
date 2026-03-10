import React from 'react'
import { Spin } from 'antd'

interface PageContainerProps {
  title: string
  subtitle?: string
  children: React.ReactNode
  extra?: React.ReactNode
  loading?: boolean
}

export function PageContainer({ title, subtitle, children, extra, loading }: PageContainerProps) {
  return (
    <div className="p-6">
      {(title || extra) && (
        <div className="flex items-center justify-between mb-6">
          <div>
            {title && <h1 className="text-2xl font-semibold text-text-primary">{title}</h1>}
            {subtitle && <p className="text-text-secondary mt-1">{subtitle}</p>}
          </div>
          {extra && <div>{extra}</div>}
        </div>
      )}
      <div className={loading ? 'min-h-[200px] flex items-center justify-center' : ''}>
        {loading ? <Spin size="large" /> : children}
      </div>
    </div>
  )
}
