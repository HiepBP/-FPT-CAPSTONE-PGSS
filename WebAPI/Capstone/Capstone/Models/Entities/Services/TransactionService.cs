using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.Models.Entities.Services
{
    public partial interface ITransactionService
    {
        IQueryable<Transaction> GetTransactionByUserId(string userId);
        Transaction CheckCode(string userId, string transactionCode, int carParkId);
        //int CreateTransaction(Transaction model);
    }

    public partial class TransactionService
    {
        public IQueryable<Transaction> GetTransactionByUserId(string userId)
        {
            var transactions = this.Get(q => q.AspNetUserId == userId)
                .OrderByDescending(q => q.Id);
            return transactions;
        }

        public Transaction CheckCode(string userId, string transactionCode, int carParkId)
        {
            var entity = this.Get(q => q.AspNetUserId == userId && q.TransactionCode == transactionCode && q.CarParkId == carParkId).FirstOrDefault();
            if(entity != null && entity.Status == (int)TransactionStatus.Reserved)
            {
                return entity;
            }
            return null;
        }

        //public int CreateTransaction(Transaction model)
        //{

        //}
    }
}